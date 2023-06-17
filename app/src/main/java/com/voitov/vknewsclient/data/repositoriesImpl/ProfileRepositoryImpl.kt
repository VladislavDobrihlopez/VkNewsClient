package com.voitov.vknewsclient.data.repositoriesImpl

import android.util.Log
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.ProfileMapper
import com.voitov.vknewsclient.data.network.ProfileApiService
import com.voitov.vknewsclient.data.network.models.profileModels.details.ProfileDataDto
import com.voitov.vknewsclient.domain.ProfileResult
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import com.voitov.vknewsclient.domain.ProfileAuthor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val mapper: ProfileMapper,
    private val storage: VKPreferencesKeyValueStorage,
    private val apiService: ProfileApiService,
) : ProfileRepository {
    private var currentUser: ProfileAuthor = ProfileAuthor.Me
    private var signedInUserProfileInfo: ProfileDataDto? = null

    private val token
        get() = VKAccessToken.restore(storage)

    private val profileDetailsFlow = flow {
        val profileDetailsDto =
            when (val user = currentUser) {
                is ProfileAuthor.OtherPerson ->
                    apiService.getProfileInfo(
                        userIds = user.authorId.toString(),
                        token = getUserToken()
                    ).response[0]

                is ProfileAuthor.Me -> signedInUserProfileInfo ?: apiService.getProfileInfo(
                    getUserToken()
                ).response[0]

            }
        Log.d("TEST_PROFILE_WALL", "profile details")
        emit(profileDetailsDto)
    }

    private val wallContentFlow = flow {
        val profileDto = when (val user = currentUser) {
            is ProfileAuthor.OtherPerson ->
                apiService.getWallContent(
                    token = getUserToken(),
                    ownerId = user.authorId.toString()
                ).content

            is ProfileAuthor.Me ->
                apiService.getWallContent(
                    token = getUserToken(),
                    ownerId = signedInUserProfileInfo?.id?.toString() ?: apiService.getProfileInfo(
                        getUserToken()
                    ).response[0].id.toString()
                ).content
        }

        // val mappedProfile = mapper.mapDtoToEntity(profileDto)
        Log.d("TEST_PROFILE_WALL", "wall")

        emit(profileDto)
    }

    private val profileDataFlow = combine(wallContentFlow, profileDetailsFlow) { content, profile ->
        Log.d("TEST_PROFILE_WALL", "successfully mapped")
        ProfileResult.Success(
            profileDetails = mapper.mapDtoToEntities(profile),
            wallContent = if (content != null) {
                mapper.mapDtoToEntities(content)
            } else {
                null
            }
        )
    }

    override fun getProfileDataFlow(author: ProfileAuthor): Flow<ProfileResult> {
        currentUser = author//"423328"//userId//"270815158"////"137554875"
        return profileDataFlow
    }

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }
}