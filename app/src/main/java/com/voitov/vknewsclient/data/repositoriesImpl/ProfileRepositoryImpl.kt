package com.voitov.vknewsclient.data.repositoriesImpl

import android.util.Log
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.ProfileMapper
import com.voitov.vknewsclient.data.network.ProfileApiService
import com.voitov.vknewsclient.domain.ProfileResult
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val mapper: ProfileMapper,
    private val storage: VKPreferencesKeyValueStorage,
    private val apiService: ProfileApiService,
    private val profileMapper: ProfileMapper
) : ProfileRepository {
    private lateinit var userId: String
    private val token
        get() = VKAccessToken.restore(storage)

    private val profileDetailsFlow = flow {
        val profileDetailsDto =
            apiService.getProfileInfo(userIds = userId, token = getUserToken()).response[0]
        Log.d("TEST_PROFILE_WALL", "profile details")

        emit(profileDetailsDto)
    }

    private val wallContentFlow = flow {
        val profileDto =
            apiService.getWallContent(token = getUserToken(), domain = userId).content
        // val mappedProfile = mapper.mapDtoToEntity(profileDto)
        Log.d("TEST_PROFILE_WALL", "wall")

        emit(profileDto)
    }

    private val profileDataFlow = combine(wallContentFlow, profileDetailsFlow) { content, profile ->
        Log.d("TEST_PROFILE_WALL", "successfully mapped")
        ProfileResult.Success(
            profileDetails = profileMapper.mapDtoToEntities(profile),
            wallContent = profileMapper.mapDtoToEntities(content)
        )
    }
//        .catch {
//        ProfileResult.Failure(it.message ?: "Unexpected error")
//    }

    override fun getProfileDataFlow(userId: String): Flow<ProfileResult> {
        this.userId = userId
        return profileDataFlow
    }

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }
}