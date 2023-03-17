package com.voitov.vknewsclient.data.network

import com.voitov.vknewsclient.data.network.models.LikesResponseDto
import com.voitov.vknewsclient.data.network.models.NewsFeedContentResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("newsfeed.getRecommended?v=${API_VERSION}")
    suspend fun loadNews(@Query("access_token") token: String): NewsFeedContentResponseDto

    @GET("newsfeed.getRecommended?v=${API_VERSION}")
    suspend fun loadNews(
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

    companion object {
        private const val API_VERSION = "5.131"
    }
}