package com.voitov.vknewsclient.data.datasources

import android.util.Log
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.CommentMapper
import com.voitov.vknewsclient.data.mappers.PostMapper
import com.voitov.vknewsclient.data.network.RecommendationsFeedApiService
import com.voitov.vknewsclient.data.network.models.postsFeedModels.NewsFeedContentResponseDto
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.CommentsResult
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostsFeedRemoteDataSourceImpl @Inject constructor(
    private val storage: VKPreferencesKeyValueStorage,
    private val recommendationsFeedApiService: RecommendationsFeedApiService,
    private val postMapper: PostMapper,
    private val commentMapper: CommentMapper
) : PostsFeedRemoteDataSource {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val token
        get() = VKAccessToken.restore(storage)

    private val _posts = mutableListOf<PostItem>()
    private val postItems: List<PostItem>
        get() = _posts.toList()

    private val _comments = mutableListOf<PostCommentItem>()
    private val commentItems: List<PostCommentItem>
        get() = _comments.toList()

    private var nextPostFrom: String? = null
    private var nextCommentOffset: Int = 0

    private val updateDataEvent = MutableSharedFlow<Unit>()
    private val updateDataFlow = flow {
        updateDataEvent.collect {
            emit(postItems)
        }
    }

    private val needNextDataEvents = MutableSharedFlow<Unit>(replay = 1)
    private val recommendations: Flow<NewsFeedResult> = flow {
        needNextDataEvents.emit(Unit)
        needNextDataEvents.collect {
            Log.d("INTERNET_TEST", "collect")

            when (retrieveFeedData()) {
                is FeedResponseResult.Success -> {
                    emit(NewsFeedResult.Success(postItems))
                }

                is FeedResponseResult.Failure -> {
                    emit(NewsFeedResult.Failure)
                }

                else -> {
                    emit(NewsFeedResult.EndOfNewsFeed)
                }
            }
        }
    }
        .retry(1) {
            delay(RETRY_DELAY_IN_MILLIS)
            Log.d("INTERNET_TEST", "retry")
            true
        }
        .catch {
            Log.d("INTERNET_TEST", "catch")
            emit(NewsFeedResult.Failure)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val combinedFlow = recommendations.flatMapLatest { result ->
        if (result is NewsFeedResult.Success) {
            flow {
                updateDataFlow.collect {
                    emit(it)
                }
            }
                .mergeWith(flowOf(result.posts))
                .map {
                    NewsFeedResult.Success(it)
                }
        } else {
            flowOf(result)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = NewsFeedResult.Success(posts = postItems)
    )

    override fun getRecommendationsFlow(): StateFlow<NewsFeedResult> {
        return combinedFlow
    }

    private val needNextCommentsEvent = MutableSharedFlow<PostItem>(replay = 1)
    private val comments = flow {
        needNextCommentsEvent.collect { post ->
            Log.d("TEST_COMMENTS_SCREEN", "needNextCommentsEvent.collect()")
            val result = retrievePostComments(post)
            emit(result)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = CommentsResult.Initial
    )

    override fun getCommentsFlow(post: PostItem): StateFlow<CommentsResult> {
        Log.d("TEST_COMMENTS_SCREEN", "getCommentsFlow()")

        nextCommentOffset = 0
        _comments.clear()
        scope.launch {
            Log.d("TEST_COMMENTS_SCREEN", "emit for getCommentsFlow()")

            needNextCommentsEvent.emit(post)
        }
        return comments
    }

    override fun getAuthStatusFlow(): Flow<AuthorizationStateResult> {
        return authStatus
    }

    override suspend fun retrieveNextRecommendations() {
        Log.d("INTERNET_TEST", "ask for next recommendations")
        needNextDataEvents.emit(Unit)
    }

    override suspend fun retrieveNextChunkOfComments(post: PostItem) {
        Log.d("TEST_COMMENTS_SCREEN", "offset: $nextCommentOffset")

        needNextCommentsEvent.emit(post)
    }

    private sealed class FeedResponseResult {
        class Success(val newsFeedContentResponseDto: NewsFeedContentResponseDto) :
            FeedResponseResult()

        object Failure : FeedResponseResult()
        object EndOfNewsFeed : FeedResponseResult()
    }

    private suspend fun retrievePostComments(post: PostItem): CommentsResult {
        if (vkPostCommentsAreOver()) {
            return CommentsResult.EndOfComments
        }

        val commentsResponseResult = try {
            val response = recommendationsFeedApiService.getComments(
                token = getUserToken(),
                ownerId = post.communityId,
                postId = post.id,
                offset = nextCommentOffset.coerceAtLeast(0)
            )
            val count = response.content.items.count()

            if (count == 0) {
                return CommentsResult.EndOfComments
            }

            nextCommentOffset += count

            val mappedComments = commentMapper.mapDtoToEntity(response)
            _comments.addAll(mappedComments)
            CommentsResult.Success(commentItems)
        } catch (ex: Exception) {
            CommentsResult.Failure(ex)
        }

        return commentsResponseResult
    }

    private suspend fun retrieveFeedData(): FeedResponseResult {
        val placeToStartLoadingFrom = nextPostFrom

        if (vkHasNothingToRecommendAnymore()) {
            return FeedResponseResult.EndOfNewsFeed
        }

        val feedResponseResult = try {
            val response = if (placeToStartLoadingFrom == null) {
                recommendationsFeedApiService.loadRecommendations(getUserToken())
            } else {
                recommendationsFeedApiService.loadRecommendations(
                    getUserToken(),
                    placeToStartLoadingFrom
                )
            }
            FeedResponseResult.Success(response)
        } catch (_: Exception) {
            FeedResponseResult.Failure
        }

        if (feedResponseResult is FeedResponseResult.Success) {
            nextPostFrom = feedResponseResult.newsFeedContentResponseDto.content.nextFrom
            val mappedPosts =
                postMapper.mapDtoResponseToEntitiesOfPostItem(feedResponseResult.newsFeedContentResponseDto)
            _posts.addAll(mappedPosts)
        }
        return feedResponseResult
    }

    private fun vkHasNothingToRecommendAnymore(): Boolean {
        return nextPostFrom == null && _posts.isNotEmpty()
    }

    private fun vkPostCommentsAreOver(): Boolean {
        return false
    }

    override suspend fun sharePostOnProfileWall(post: PostItem) {
        val updatedMetricsInfo = recommendationsFeedApiService.sharePost(
            token = getUserToken(),
            sharedObject = "wall${post.communityId}_${post.id}"
        ).response

        val updatedMetrics = post.metrics.toMutableList().apply {
            removeIf { it.type == MetricsType.LIKES }
            add(SocialMetric(MetricsType.LIKES, updatedMetricsInfo.likes))
            removeIf { it.type == MetricsType.SHARES }
            add(SocialMetric(MetricsType.SHARES, updatedMetricsInfo.shares))
        }
        val updatedPost =
            post.copy(isSharedByUser = true, isLikedByUser = true, metrics = updatedMetrics)
        val indexOfElementToBeReplaced = _posts.indexOf(post)
        if (indexOfElementToBeReplaced == -1) {
            return
        }

        _posts[indexOfElementToBeReplaced] = updatedPost
        updateDataEvent.emit(Unit)
    }

    override suspend fun reverseLikeStatus(post: PostItem) {
        val updatedLikesCount = if (post.isLikedByUser) {
            recommendationsFeedApiService.removeLike(
                token = getUserToken(),
                type = PostItem.TYPE,
                ownerId = post.communityId,
                itemId = post.id
            ).likes.count
        } else {
            recommendationsFeedApiService.addLike(
                token = getUserToken(),
                type = PostItem.TYPE,
                ownerId = post.communityId,
                itemId = post.id
            ).likes.count
        }

        val updatedMetrics = post.metrics.toMutableList().apply {
            removeIf { it.type == MetricsType.LIKES }
            add(SocialMetric(MetricsType.LIKES, updatedLikesCount))
        }
        val updatedPost = post.copy(isLikedByUser = !post.isLikedByUser, metrics = updatedMetrics)
        val indexOfElementToBeReplaced = _posts.indexOf(post)
        if (indexOfElementToBeReplaced == -1) {
            return
        }

        _posts[indexOfElementToBeReplaced] = updatedPost
        updateDataEvent.emit(Unit)
    }

    override suspend fun ignoreItem(post: PostItem) {
        recommendationsFeedApiService.ignoreItem(
            token = getUserToken(),
            ownerId = post.communityId,
            ignoredItemId = post.id
        )
        _posts.remove(post)
        updateDataEvent.emit(Unit)
    }

//    private fun loadComments(post: PostItem): Flow<List<PostCommentItem>> = flow {
//        val res = recommendationsFeedApiService.getComments(
//            token = getUserToken(),
//            ownerId = post.communityId,
//            postId = post.id,
//            startCommentId = 0
//        )
//        emit(commentMapper.mapDtoToEntity(res))
//    }
//        .retry(10) {
//            delay(RETRY_DELAY_IN_MILLIS)
//            true
//        }
    //.stateIn(scope = scope, started = SharingStarted.Lazily, initialValue = listOf())

//    override fun getPostTags(): Flow<List<ItemTag>> {
//        return flow {
//            val tags = mutableListOf<ItemTag>()
//            for (i in 1..10) {
//                tags.add(ItemTag("tag $i"))
//            }
//            emit(tags)
//        }
//    }

    private val checkAuthStatusEvent = MutableSharedFlow<Unit>(replay = 1)
    override suspend fun retrySigningIn() {
        checkAuthStatusEvent.emit(Unit)
    }

    private val authStatus: Flow<AuthorizationStateResult> = flow {
        checkAuthStatusEvent.emit(Unit)
        checkAuthStatusEvent.collect {
            val currentToken = token
            val authState = if (currentToken != null && currentToken.isValid) {
                AuthorizationStateResult.AuthorizationStateSuccess
            } else {
                AuthorizationStateResult.AuthorizationStateFailure
            }
            emit(authState)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = AuthorizationStateResult.InitialState
    )

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }

    companion object {
        private const val RETRY_DELAY_IN_MILLIS = 7000L
    }
}