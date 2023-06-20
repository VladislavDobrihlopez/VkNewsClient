package com.voitov.vknewsclient.data.repositoriesImpl

import android.util.Log
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.voitov.vknewsclient.data.mappers.ProfileMapper
import com.voitov.vknewsclient.data.network.ProfileApiService
import com.voitov.vknewsclient.data.network.models.postsFeedModels.GroupInfoDto
import com.voitov.vknewsclient.data.network.models.postsFeedModels.ProfileDto
import com.voitov.vknewsclient.data.network.models.profileModels.details.ProfileDataDto
import com.voitov.vknewsclient.data.network.models.profileModels.wall.ProfileWallContentDto
import com.voitov.vknewsclient.data.network.models.profileModels.wall.WallPostDto
import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.ProfileResult
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val mapper: ProfileMapper,
    private val storage: VKPreferencesKeyValueStorage,
    private val apiService: ProfileApiService,
) : ProfileRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var currentUser: ProfileAuthor = ProfileAuthor.Me
    private var signedInUserProfileInfo: ProfileDataDto? = null

    private var _cachedWallPosts = HashSet<WallPostDto>()
    private val cachedWallPosts
        get() = _cachedWallPosts.toList()

    private val _cachedGroupInfo = HashSet<GroupInfoDto>()
    private val cachedGroupInfo
        get() = _cachedGroupInfo.toList()
    private val _cachedProfile = HashSet<ProfileDto>()
    private val cachedProfile
        get() = _cachedProfile.toList()

    private var cachedUserProfileDataDto: ProfileDataDto? = null

    private var nextPostsOffset: Int = 0

    private val token
        get() = VKAccessToken.restore(storage)

    private val wallEventContainer = MutableSharedFlow<Unit>(replay = 1)

    private val wallContentFlow = flow {
        wallEventContainer.emit(Unit)
        wallEventContainer.collect {
            try {
                Log.d("TEST_PROFILE_WALL", "wallContentFlow")

                val firstJob = scope.async {
                    retrieveWallContent(currentUser)
                }

                val secondJob = scope.async {
                    retrieveProfileInfo(currentUser)
                }

                val retrievedChunkOfWall = firstJob.await()
                val retrievedProfileDetailsDto = secondJob.await()

                if (retrievedChunkOfWall == null) {
                    // private profile
                    emit(
                        ProfileResult.Success(
                            mapper.mapDtoToEntities(retrievedProfileDetailsDto),
                            null
                        )
                    )
                    return@collect
                } else if (retrievedChunkOfWall.posts.isEmpty()) {
                    emit(ProfileResult.EndOfWallPosts(mapper.mapDtoToEntities(retrievedProfileDetailsDto)))
                    return@collect
                } else {
                    nextPostsOffset += retrievedChunkOfWall.posts.count()
                    cacheRetrievedData(retrievedChunkOfWall)
                }

                Log.d("TEST_PROFILE_WALL", "$nextPostsOffset")
                Log.d(
                    "TEST_PROFILE_WALL",
                    "${cachedWallPosts.count()} ${cachedGroupInfo.count()} ${cachedProfile.count()}"
                )

                Log.d(
                    "TEST_PROFILE_WALL",
                    "subscribers: ${wallEventContainer.subscriptionCount.value}"
                )

                val fullWallDto =
                    ProfileWallContentDto(cachedWallPosts, cachedGroupInfo, cachedProfile)

                emit(
                    ProfileResult.Success(
                        profileDetails = mapper.mapDtoToEntities(retrievedProfileDetailsDto),
                        wallContent = mapper.mapDtoToEntities(fullWallDto)
                    )
                )
            } catch (ex: Exception) {
                emit(ProfileResult.Failure(ex))
            }
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
    )

    private suspend fun retrieveWallContent(author: ProfileAuthor): ProfileWallContentDto? {
        return when (author) {
            is ProfileAuthor.OtherPerson ->
                apiService.getWallContent(
                    token = getUserToken(),
                    ownerId = author.authorId.toString(),//"137554875",//author.authorId.toString(),
                    offset = nextPostsOffset
                ).content

            is ProfileAuthor.Me ->
                apiService.getWallContent(
                    token = getUserToken(),
                    ownerId = getSignedUser().id.toString(),
                    offset = nextPostsOffset
                ).content
        }
    }

    private suspend fun retrieveProfileInfo(author: ProfileAuthor): ProfileDataDto {
        return when (author) {
            is ProfileAuthor.OtherPerson ->
                apiService.getProfileInfo(
                    userIds = author.authorId.toString(), //"137554875",//
                    token = getUserToken()
                ).response[0]

            is ProfileAuthor.Me -> getSignedUser()
        }
    }

    private suspend fun getSignedUser(): ProfileDataDto {
        signedInUserProfileInfo?.let {
            return it
        }
        return apiService.getProfileInfo(
            getUserToken()
        ).response[0].also {
            signedInUserProfileInfo = it
        }
    }

    private fun cacheRetrievedData(profileResponse: ProfileWallContentDto) {
        _cachedWallPosts.addAll(profileResponse.posts)
        _cachedGroupInfo.addAll(profileResponse.communities)
        _cachedProfile.addAll(profileResponse.profiles)
    }

    private fun clearCache() {
        _cachedWallPosts.clear()
        _cachedGroupInfo.clear()
        _cachedProfile.clear()
    }

    override fun getProfileDataFlow(author: ProfileAuthor): SharedFlow<ProfileResult> {
        Log.d("TEST_PROFILE_WALL", "getProfileDataFlow")
        //  Log.d("TEST_PROFILE_WALL", "${this@ProfileRepositoryImpl}")
        currentUser = author//"423328"//userId//"270815158"////"137554875"
        clearCache()
        nextPostsOffset = if (author is ProfileAuthor.Me) 1 else 0
        return wallContentFlow
    }

    override suspend fun retrieveNextChunkOfWallPosts() {
        Log.d("TEST_PROFILE_WALL", "retrieveNextChunkOfWallPosts")
        // Log.d("TEST_PROFILE_WALL", "${this@ProfileRepositoryImpl}")
        Log.d("TEST_PROFILE_WALL", "subscribers: ${wallEventContainer.subscriptionCount.value}")
        delay(1500)
        wallEventContainer.emit(Unit)
    }

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }
}