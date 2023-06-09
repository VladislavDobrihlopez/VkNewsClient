package com.voitov.vknewsclient.domain.usecases.profile

import com.voitov.vknewsclient.domain.entities.Profile
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileInfoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(userId: String): Flow<Profile> {
        return repository.getProfileFlow(userId)
    }
}