package com.voitov.vknewsclient.presentation.profileScreen

import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost

sealed class ProfileScreenState {
    data class Success(val profileDetails: Profile, val wallContent: List<WallPost>) :
        ProfileScreenState()

    data class Failure(val error: String) : ProfileScreenState()
    object Initial : ProfileScreenState()
}