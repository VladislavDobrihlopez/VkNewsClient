package com.voitov.vknewsclient.presentation.profileScreen

import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost

sealed class ProfileScreenState {
    sealed class Success() : ProfileScreenState() {
        data class FreeProfile(val profileDetails: Profile, val wallContent: List<WallPost>) :
            Success()

        data class PrivateProfile(val profileDetails: Profile) : Success()
    }

    data class Failure(val error: String) : ProfileScreenState()
    object Initial : ProfileScreenState()
}