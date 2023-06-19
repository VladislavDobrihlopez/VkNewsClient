package com.voitov.vknewsclient.domain.usecases.profile

import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.ProfileResult
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetProfileInfoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(author: ProfileAuthor): SharedFlow<ProfileResult> {
        return repository.getProfileDataFlow(author)
    }
}