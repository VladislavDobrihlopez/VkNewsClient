package com.voitov.vknewsclient.presentation.profileScreen

import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost

sealed class ProfileScreenState {
    sealed class SuccessState(
        open val profileDetails: Profile,
        open val wallContent: List<WallPost>,
    ) : ProfileScreenState() {
        sealed class ProfileWithWall(
            override val profileDetails: Profile,
            override val wallContent: List<WallPost>,
            open val isDataBeingLoaded: Boolean = false
        ) : SuccessState(profileDetails, wallContent) {
            data class FreeProfileState(
                override val profileDetails: Profile,
                override val wallContent: List<WallPost>,
                override val isDataBeingLoaded: Boolean = false
            ) : ProfileWithWall(profileDetails, wallContent, isDataBeingLoaded)

            data class EndOfPostsState(
                override val profileDetails: Profile,
                override val wallContent: List<WallPost>,
            ) : ProfileWithWall(profileDetails, wallContent, false)
        }

        data class PrivateProfileState(
            override val profileDetails: Profile
        ) : SuccessState(profileDetails, listOf())
    }

    data class FailureState(val error: String) : ProfileScreenState()
    object InitialState : ProfileScreenState()
    object LoadingState : ProfileScreenState()
}