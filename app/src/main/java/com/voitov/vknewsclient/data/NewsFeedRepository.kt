package com.voitov.vknewsclient.data

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.PostMapper
import com.voitov.vknewsclient.data.network.ApiFactory
import com.voitov.vknewsclient.data.network.models.LikesResponseDto
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostItem
import kotlinx.coroutines.delay

class NewsFeedRepository(application: Application) {
    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)
    private val apiService = ApiFactory.apiService
    private val mapper = PostMapper()

    private val _posts = mutableListOf<PostItem>()
    val posts: List<PostItem>
        get() = _posts.toList()

    suspend fun loadRecommendations(): List<PostItem> {
        val response = apiService.loadNews(getUserToken())
        val mappedPosts = mapper.mapDtoResponseToEntitiesOfPostItem(response)
        _posts.clear()
        _posts.addAll(mappedPosts)
        return mappedPosts
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

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }
}