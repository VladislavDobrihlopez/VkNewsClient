package com.voitov.vknewsclient.data

import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.CommentMapper
import com.voitov.vknewsclient.data.mappers.PostMapper
import com.voitov.vknewsclient.data.network.ApiService
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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
    private val recommendations: StateFlow<NewsFeedResult> = flow {
        needNextDataEvents.emit(Unit)
        needNextDataEvents.collect {
            retrieveData()
            emit(posts)
        }
    }
        .mergeWith(updateDataFlow)
        .map {
            NewsFeedResult.Success(posts = it) as NewsFeedResult
        }
        .retry(10) {
            delay(RETRY_DELAY_IN_MILLIS)
            true
        }
        .catch { emit(NewsFeedResult.Failure) }
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = NewsFeedResult.Success(posts = posts)
        )

    override fun getRecommendationsFlow(): StateFlow<NewsFeedResult> {
        return recommendations
    }

    override fun getCommentsFlow(post: PostItem): Flow<List<PostCommentItem>> {
        return loadComments(post)
    }

    override fun getAuthStatusFlow(): Flow<AuthorizationStateResult> {
        return authStatus
    }

    override suspend fun retrieveNextRecommendations() {
        needNextDataEvents.emit(Unit)
    }

    private suspend fun retrieveData() {
        val placeToStartLoadingFrom = nextPostFrom

        if (vkHasNothingToRecommendAnymore()) {
            return
        }

        val response = if (placeToStartLoadingFrom == null) {
            apiService.loadNews(getUserToken())
        } else {
            apiService.loadNews(getUserToken(), placeToStartLoadingFrom)
        }

        nextPostFrom = response.content.nextFrom
        val mappedPosts = postMapper.mapDtoResponseToEntitiesOfPostItem(response)
        _posts.addAll(mappedPosts)
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