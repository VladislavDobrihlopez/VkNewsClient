package com.voitov.vknewsclient.data.network

import com.voitov.vknewsclient.data.network.models.NewsFeedContentResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadNews(@Query("access_token") token: String): NewsFeedContentResponseDto
}