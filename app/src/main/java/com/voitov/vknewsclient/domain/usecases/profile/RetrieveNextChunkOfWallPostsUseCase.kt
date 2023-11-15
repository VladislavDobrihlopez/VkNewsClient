package com.voitov.vknewsclient.domain.usecases.profile

import com.voitov.vknewsclient.domain.repository.ProfileRepository
import javax.inject.Inject

class RetrieveNextChunkOfWallPostsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() {
        repository.retrieveNextChunkOfWallPosts()
    }
}