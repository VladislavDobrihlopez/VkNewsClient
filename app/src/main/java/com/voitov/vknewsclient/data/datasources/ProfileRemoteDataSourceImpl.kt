package com.voitov.vknewsclient.data.datasources

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val mapper: ProfileMapper,
    private val storage: VKPreferencesKeyValueStorage,
    private val apiService: ProfileApiService,
) : ProfileRemoteDataSource {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var currentUser: ProfileAuthor = ProfileAuthor.Me
    private var signedInUserProfileInfo: ProfileDataDto? = null

    private var _retrievedSoFarWallPosts = HashSet<WallPostDto>()
    private val retrievedSoFarWallPosts
        get() = _retrievedSoFarWallPosts.toList().sortedBy {
            -it.secondsSince1970
        }

    private var _retrievedSoFarGroupInfo = HashSet<GroupInfoDto>()
    private val retrievedSoFarGroupInfo
        get() = _retrievedSoFarGroupInfo.toList()
    private var _retrievedSoFarProfile = HashSet<ProfileDto>()
    private val retrievedSoFarProfile
        get() = _retrievedSoFarProfile.toList()

    private var nextPostsOffset: Int = 0

    private val token
        get() = VKAccessToken.restore(storage)

    private val wallEventContainer = MutableSharedFlow<Unit>(replay = 1)

    private val wallContentFlow = flow {
        wallEventContainer.emit(Unit)
        wallEventContainer.collect {
            try {
                val retrievedChunkOfWall = coroutineScope {
                    async {
                        retrieveWallContent(currentUser)
                    }
                }.await()

                val retrievedProfileDetailsDto = coroutineScope {
                    async {
                        retrieveProfileInfo(currentUser)
                    }
                }.await()

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
                    emit(
                        ProfileResult.EndOfWallPosts(
                            mapper.mapDtoToEntities(
                                retrievedProfileDetailsDto
                            )
                        )
                    )
                    return@collect
                } else {
                    nextPostsOffset += retrievedChunkOfWall.posts.count()
                    cacheRetrievedData(retrievedChunkOfWall)
                }

                val fullWallDto =
                    ProfileWallContentDto(
                        retrievedSoFarWallPosts,
                        retrievedSoFarGroupInfo,
                        retrievedSoFarProfile
                    )

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
        _retrievedSoFarWallPosts.addAll(profileResponse.posts)
        _retrievedSoFarGroupInfo.addAll(profileResponse.communities)
        _retrievedSoFarProfile.addAll(profileResponse.profiles)
    }

    private fun clearCache() {
        _retrievedSoFarWallPosts = HashSet()
        _retrievedSoFarGroupInfo = HashSet()
        _retrievedSoFarProfile = HashSet()
    }

    override fun getProfileDataFlow(author: ProfileAuthor): SharedFlow<ProfileResult> {
        currentUser = author//"423328"//userId//"270815158"////"137554875"
        clearCache()
        nextPostsOffset = if (author is ProfileAuthor.Me) 1 else 0
        return wallContentFlow
    }

    override suspend fun retrieveNextChunkOfWallPosts() {
        delay(1500)
        wallEventContainer.emit(Unit)
    }

    private fun getUserToken(): String {
        return token?.accessToken
            ?: throw IllegalStateException("Token is null. But it is now allowed")
    }
}