package com.voitov.vknewsclient.presentation.profileScreen

import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost

sealed class ProfileScreenState {
    sealed class SuccessState : ProfileScreenState() {
        data class FreeProfileState(
            val profileDetails: Profile,
            val wallContent: List<WallPost>,
            val isDataBeingLoaded: Boolean = false
        ) : SuccessState()

        data class EndOfPostsState(
            val profileDetails: Profile,
            val wallContent: List<WallPost>
        ) : SuccessState()

        data class PrivateProfileState(
            val profileDetails: Profile
        ) : SuccessState()
    }

    data class FailureState(val error: String) : ProfileScreenState()
    object InitialState : ProfileScreenState()
    object LoadingState : ProfileScreenState()
}