package com.voitov.vknewsclient.data.network

import com.voitov.vknewsclient.data.network.models.profileModels.details.ProfileResponseDto
import com.voitov.vknewsclient.data.network.models.profileModels.wall.ProfileWallContentResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApiService {
    @GET("users.get?v=${API_VERSION}")
    suspend fun getProfileInfo(
        @Query("access_token") token: String,
        @Query("user_ids") userIds: String,
        @Query("fields") fields: String = FIELDS
    ): ProfileResponseDto

    @GET("users.get?v=${API_VERSION}")
    suspend fun getProfileInfo(
        @Query("access_token") token: String,
        @Query("fields") fields: String = FIELDS
    ): ProfileResponseDto

    @GET("wall.get?v=${API_VERSION}&count=20&extended=1")
    suspend fun getWallContent(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: String,
        @Query("offset") offset: Int,
    ): ProfileWallContentResponseDto

    companion object {
        private const val API_VERSION = "5.131"
        private val FIELDS = listOf(
            "domain",
            "bdate",
            "about",
            "city",
            "country",
            "career",
            "photo_max",
            "online",
            "followers_count",
            "deactivated",
            "cover",
            "counters"
        ).joinToString(",")
    }
}