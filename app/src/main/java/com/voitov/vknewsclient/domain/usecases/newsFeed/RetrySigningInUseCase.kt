package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import javax.inject.Inject

class RetrySigningInUseCase @Inject constructor(
    private val repository: PostsFeedRepository
) {
    suspend operator fun invoke() {
        repository.retrySigningIn()
    }
}