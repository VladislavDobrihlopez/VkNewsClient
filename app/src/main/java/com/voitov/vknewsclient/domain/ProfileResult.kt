package com.voitov.vknewsclient.domain

import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.entities.WallPost

sealed class ProfileResult {
    data class Success(val profileDetails: Profile, val wallContent: List<WallPost>?) :
        ProfileResult()

    data class Failure(val ex: Throwable) : ProfileResult()

    object EndOfWallPosts : ProfileResult()
    object Initial : ProfileResult()
}
