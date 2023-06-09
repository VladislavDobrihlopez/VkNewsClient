package com.voitov.vknewsclient.presentation.profileScreen

import com.voitov.vknewsclient.domain.entities.Profile

sealed class ProfileScreenState {
    data class Success(val profileInfo: Profile) : ProfileScreenState()
    data class Failure(val errorMessage: String) : ProfileScreenState()
}