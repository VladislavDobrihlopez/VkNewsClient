package com.voitov.vknewsclient.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private val okHttpClient =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
    private const val BASE_URL = "https://api.vk.com/method/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val postsFeedApiService: PostsFeedApiService = retrofit.create(PostsFeedApiService::class.java)
    val profileApiService: ProfileApiService = retrofit.create(ProfileApiService::class.java)
}