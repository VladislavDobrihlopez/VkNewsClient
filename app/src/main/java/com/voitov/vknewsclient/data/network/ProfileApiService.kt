package com.voitov.vknewsclient.data.network

import com.voitov.vknewsclient.data.network.models.profileModels.ProfileResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApiService {
    @GET("users.get?v=${API_VERSION}")
    suspend fun getProfileInfo(
        @Query("user_ids") userIds: String,
        @Query("access_token") token: String,
        @Query("fields") fields: String = FIELDS
    ): ProfileResponseDto

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
            "deactivated"
        ).joinToString(",")
    }
}