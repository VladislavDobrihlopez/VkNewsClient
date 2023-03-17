package com.voitov.vknewsclient.data

import android.app.Application
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.PostMapper
import com.voitov.vknewsclient.data.network.ApiFactory
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostItem

class NewsFeedRepository(application: Application) {
    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)
    private val apiService = ApiFactory.apiService
    private val mapper = PostMapper()

    private val _posts = mutableListOf<PostItem>()
    val posts: List<PostItem>
        get() = _posts.toList()

    private var nextFrom: String? = null

    suspend fun loadRecommendations(): List<PostItem> {
        val placeToStartLoadingFrom = nextFrom

        if (vkHasNothingToRecommendAnymore()) {
            return _posts
        }

        val response = if (placeToStartLoadingFrom == null) {
            apiService.loadNews(getUserToken())
        } else {
            apiService.loadNews(getUserToken(), placeToStartLoadingFrom)
        }

        nextFrom = response.content.nextFrom
        val mappedPosts = mapper.mapDtoResponseToEntitiesOfPostItem(response)
        _posts.addAll(mappedPosts)

        return posts
    }

    private fun vkHasNothingToRecommendAnymore(): Boolean {
        return nextFrom == null && _posts.isNotEmpty()
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
    }

    suspend fun ignoreItem(post: PostItem) {
        apiService.ignoreItem(
            token = getUserToken(),
            ownerId = post.communityId,
            ignoredItemId = post.id
        )
        _posts.remove(post)
    }

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }
}