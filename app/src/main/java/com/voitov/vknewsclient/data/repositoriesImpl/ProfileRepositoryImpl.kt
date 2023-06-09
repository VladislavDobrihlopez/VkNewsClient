package com.voitov.vknewsclient.data.repositoriesImpl

import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.ProfileMapper
import com.voitov.vknewsclient.data.network.ProfileApiService
import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val mapper: ProfileMapper,
    private val storage: VKPreferencesKeyValueStorage,
    private val apiService: ProfileApiService
) : ProfileRepository {
    private var userId: String = ""
    private val token
        get() = VKAccessToken.restore(storage)

    private val profileFlow = flow {
        val profileDto = apiService.getProfileInfo(userId, token = getUserToken()).response[0]
        val mappedProfile = mapper.mapDtoToEntity(profileDto)
        emit(mappedProfile)
    }

    override fun getProfileFlow(userId: String): Flow<Profile> {
        this.userId = userId
        return profileFlow
    }

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }
}