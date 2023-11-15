package com.voitov.vknewsclient.presentation.profileScreen

import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost

sealed class ProfileScreenState {
    sealed class SuccessState(
        open val profileDetails: Profile,
        open val wallContent: List<WallPost>,
    ) : ProfileScreenState() {
        sealed class ProfileWithPublicAccessToWallState(
            override val profileDetails: Profile,
            override val wallContent: List<WallPost>,
            open val isDataBeingLoaded: Boolean = false
        ) : SuccessState(profileDetails, wallContent) {
            data class Success(
                override val profileDetails: Profile,
                override val wallContent: List<WallPost>,
                override val isDataBeingLoaded: Boolean = false
            ) : ProfileWithPublicAccessToWallState(profileDetails, wallContent, isDataBeingLoaded)

            data class EndOfPosts(
                override val profileDetails: Profile,
                override val wallContent: List<WallPost>,
            ) : ProfileWithPublicAccessToWallState(profileDetails, wallContent, false)
        }

        data class PrivateProfile(
            override val profileDetails: Profile
        ) : SuccessState(profileDetails, listOf())
    }

    data class Failure(val error: String) : ProfileScreenState()
    object Initial : ProfileScreenState()
    object Loading : ProfileScreenState()
}