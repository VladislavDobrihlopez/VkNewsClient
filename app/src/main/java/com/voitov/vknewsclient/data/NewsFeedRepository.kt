package com.voitov.vknewsclient.data

import android.app.Application
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.CommentMapper
import com.voitov.vknewsclient.data.mappers.PostMapper
import com.voitov.vknewsclient.data.network.ApiFactory
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class NewsFeedRepository(application: Application) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)
    private val apiService = ApiFactory.apiService
    private val postMapper = PostMapper()
    private val commentMapper = CommentMapper()

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
    val recommendations: StateFlow<List<PostItem>> = flow {
        needNextDataEvents.emit(Unit)
        needNextDataEvents.collect {
            retrieveData()
            emit(posts)
        }
    }
        .mergeWith(updateDataFlow)
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = posts
        )

    suspend fun retrieveNextRecommendations() {
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

    suspend fun changeLikeStatus(post: PostItem) {
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

    suspend fun ignoreItem(post: PostItem) {
        apiService.ignoreItem(
            token = getUserToken(),
            ownerId = post.communityId,
            ignoredItemId = post.id
        )
        _posts.remove(post)
        updateDataEvent.emit(Unit)
    }

    suspend fun loadComments(post: PostItem): List<PostCommentItem> {
        val res = apiService.getComments(
            token = getUserToken(),
            ownerId = post.communityId,
            postId = post.id,
            startCommentId = 0
        )
        return commentMapper.mapDtoToEntity(res)
    }

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }
}