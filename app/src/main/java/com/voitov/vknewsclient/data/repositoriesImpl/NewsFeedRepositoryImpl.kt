package com.voitov.vknewsclient.data.repositoriesImpl

import android.util.Log
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.CommentMapper
import com.voitov.vknewsclient.data.mappers.PostMapper
import com.voitov.vknewsclient.data.network.ApiService
import com.voitov.vknewsclient.data.network.models.NewsFeedContentResponseDto
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
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
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val storage: VKPreferencesKeyValueStorage,
    private val apiService: ApiService,
    private val postMapper: PostMapper,
    private val commentMapper: CommentMapper
) : NewsFeedRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val token
        get() = VKAccessToken.restore(storage)

    private val _posts = mutableListOf<PostItem>()
    private val posts: List<PostItem>
        get() = _posts.toList()

    private var nextPostFrom: String? = null
    private var nextCommentFrom: String? = null

    private val updateDataEvent = MutableSharedFlow<Unit>()
    private val updateDataFlow = flow {
        updateDataEvent.collect {
            emit(posts)
        }
    }

    private val needNextDataEvents = MutableSharedFlow<Unit>(replay = 1)
    private val recommendations: Flow<NewsFeedResult> = flow {
        needNextDataEvents.emit(Unit)
        needNextDataEvents.collect {
            Log.d("INTERNET_TEST", "collect")

            when (retrieveData()) {
                is Result.Success -> {
                    emit(NewsFeedResult.Success(posts))
                }

                is Result.Failure -> {
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
        initialValue = NewsFeedResult.Success(posts = posts)
    )

    override fun getRecommendationsFlow(): StateFlow<NewsFeedResult> {
        return combinedFlow
    }

    override fun getCommentsFlow(post: PostItem): Flow<List<PostCommentItem>> {
        return loadComments(post)
    }

    override fun getAuthStatusFlow(): Flow<AuthorizationStateResult> {
        return authStatus
    }

    override suspend fun retrieveNextRecommendations() {
        Log.d("INTERNET_TEST", "ask for next recommendations")
        needNextDataEvents.emit(Unit)
    }

    private sealed class Result {
        class Success(val newsFeedContentResponseDto: NewsFeedContentResponseDto) : Result()
        object Failure : Result()
        object EndOfNewsFeed : Result()
    }

    private suspend fun retrieveData(): Result {
        val placeToStartLoadingFrom = nextPostFrom

        if (vkHasNothingToRecommendAnymore()) {
            return Result.EndOfNewsFeed
        }

        val result = try {
            val response = if (placeToStartLoadingFrom == null) {
                apiService.loadNews(getUserToken())
            } else {
                apiService.loadNews(getUserToken(), placeToStartLoadingFrom)
            }
            Result.Success(response)
        } catch (_: Exception) {
            Result.Failure
        }

        if (result is Result.Success) {
            nextPostFrom = result.newsFeedContentResponseDto.content.nextFrom
            val mappedPosts =
                postMapper.mapDtoResponseToEntitiesOfPostItem(result.newsFeedContentResponseDto)
            _posts.addAll(mappedPosts)
        }
        return result
    }

    private fun vkHasNothingToRecommendAnymore(): Boolean {
        return nextPostFrom == null && _posts.isNotEmpty()
    }

    override suspend fun changeLikeStatus(post: PostItem) {
        val updatedLikesCount = if (post.isLikedByUser) {
            apiService.removeLike(
                token = getUserToken(),
                type = PostItem.TYPE,
                ownerId = post.communityId,
                itemId = post.id
            ).likes.count
        } else {
            apiService.addLike(
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
        apiService.ignoreItem(
            token = getUserToken(),
            ownerId = post.communityId,
            ignoredItemId = post.id
        )
        _posts.remove(post)
        updateDataEvent.emit(Unit)
    }

    private fun loadComments(post: PostItem): Flow<List<PostCommentItem>> = flow {
        val res = apiService.getComments(
            token = getUserToken(),
            ownerId = post.communityId,
            postId = post.id,
            startCommentId = 0
        )
        emit(commentMapper.mapDtoToEntity(res))
    }
        .retry(10) {
            delay(RETRY_DELAY_IN_MILLIS)
            true
        }
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