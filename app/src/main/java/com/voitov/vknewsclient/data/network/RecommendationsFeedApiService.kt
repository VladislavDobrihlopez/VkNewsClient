package com.voitov.vknewsclient.data.network

import com.voitov.vknewsclient.data.network.models.postsFeedModels.CommentsContentResponseDto
import com.voitov.vknewsclient.data.network.models.postsFeedModels.LikesResponseDto
import com.voitov.vknewsclient.data.network.models.postsFeedModels.NewsFeedContentResponseDto
import com.voitov.vknewsclient.data.network.models.postsFeedModels.SharesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendationsFeedApiService {
    @GET("newsfeed.getRecommended?v=${API_VERSION}")
    suspend fun loadRecommendations(@Query("access_token") token: String): NewsFeedContentResponseDto

    @GET("newsfeed.getRecommended?v=${API_VERSION}")
    suspend fun loadRecommendations(
        @Query("access_token") token: String,
        @Query("start_from") startFrom: String
    ): NewsFeedContentResponseDto

    @GET("likes.add?v=${API_VERSION}")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("type") type: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long
    ): LikesResponseDto

    @GET("wall.repost?v=${API_VERSION}")
    suspend fun sharePost(
        @Query("access_token") token: String,
        @Query("object") sharedObject: String,
        @Query("message") message: String = "",
    ): SharesResponseDto

    @GET("likes.delete?v=${API_VERSION}")
    suspend fun removeLike(
        @Query("access_token") token: String,
        @Query("type") type: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long
    ): LikesResponseDto

    @GET("newsfeed.ignoreItem?v=${API_VERSION}")
    suspend fun ignoreItem(
        @Query("access_token") token: String,
        @Query("type") type: String = "wall",
        @Query("owner_id") ownerId: Long,
        @Query("item_id") ignoredItemId: Long
    )

    @GET("wall.getComments?v=${API_VERSION}&need_likes=1&extended=1&fields=photo_100&count=15")
    suspend fun getComments(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
        @Query("offset") offset: Int,
        @Query("sort") sortOrder: String = "asc"
    ): CommentsContentResponseDto

    companion object {
        private const val API_VERSION = "5.131"
    }
}