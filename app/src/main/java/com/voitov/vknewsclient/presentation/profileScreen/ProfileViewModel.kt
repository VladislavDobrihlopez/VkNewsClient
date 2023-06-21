package com.voitov.vknewsclient.presentation.profileScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.ProfileResult
import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost
import com.voitov.vknewsclient.domain.usecases.profile.GetProfileInfoUseCase
import com.voitov.vknewsclient.domain.usecases.profile.RetrieveNextChunkOfWallPostsUseCase
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileAuthor: ProfileAuthor,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val retrieveNextChunkOfWallPostsUseCase: RetrieveNextChunkOfWallPostsUseCase
) : ViewModel() {
    init {
        Log.d("TEST_PROFILE_WALL", "creating viewmodel: $this")
    }
    private var viewedAllPosts = false
    private lateinit var cachedProfileDetails: Profile
    private var cachedPosts: List<WallPost>? = null
    private val eventContainer = MutableSharedFlow<Unit>()
    private val cachedDataFlow = flow {
        eventContainer.collect {
            emit(
                ProfileScreenState.SuccessState.ProfileWithPublicAccessToWallState.Success(
                    profileDetails = cachedProfileDetails,
                    wallContent = cachedPosts ?: throw IllegalStateException(),
                    isDataBeingLoaded = true
                )
            )
        }
    }
    val profileFlow = getProfileInfoUseCase(profileAuthor)
        .map {
            Log.d("TEST_PROFILE_WALL", "OnEach ${it.javaClass}")
            when (it) {
                is ProfileResult.Success -> if (it.wallContent != null) {
                    Log.d("TEST_PROFILE_WALL", "OnEach ${it.profileDetails.firstName}")

                    ProfileScreenState.SuccessState.ProfileWithPublicAccessToWallState.Success(
                        profileDetails = it.profileDetails,
                        wallContent = it.wallContent
                    )
                } else {
                    ProfileScreenState.SuccessState.PrivateProfile(
                        profileDetails = it.profileDetails,
                    )
                }

                is ProfileResult.Failure -> ProfileScreenState.Failure(
                    error = it.ex.toString() + it.ex.stackTraceToString()
                )

                is ProfileResult.EndOfWallPosts -> {
                    viewedAllPosts = true

                    val currentCache = cachedPosts
                    if (currentCache == null) { // in case of empty wall and public access
                        ProfileScreenState.SuccessState.ProfileWithPublicAccessToWallState.EndOfPosts(
                            profileDetails = it.profileDetails,
                            wallContent = listOf()
                        )
                    } else {
                        ProfileScreenState.SuccessState.ProfileWithPublicAccessToWallState.EndOfPosts(
                            profileDetails = cachedProfileDetails,
                            wallContent = currentCache
                        )
                    }
                }

                is ProfileResult.Initial -> {
                    ProfileScreenState.Initial
                }
            }
        }
        .onEach { state ->
            when (state) {
                is ProfileScreenState.SuccessState.ProfileWithPublicAccessToWallState.Success -> {
                    cachedPosts = state.wallContent
                    cachedProfileDetails = state.profileDetails
                }

                is ProfileScreenState.SuccessState.PrivateProfile -> {
                    cachedProfileDetails = state.profileDetails
                }

                is ProfileScreenState.SuccessState.ProfileWithPublicAccessToWallState.EndOfPosts -> {
                    cachedPosts = state.wallContent
                    cachedProfileDetails = state.profileDetails
                }

                else -> {}
            }
        }
        .onStart {
            emit(ProfileScreenState.Loading)
            delay(100)
        }
        .mergeWith(cachedDataFlow)

    fun loadContinuingWallPosts() {
        viewModelScope.launch {
            Log.d("TEST_PROFILE_WALL", "viewmodel: next chunk")
            if (!viewedAllPosts) {
                eventContainer.emit(Unit)
                retrieveNextChunkOfWallPostsUseCase()
            }
        }
    }
}


