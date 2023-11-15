package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStatusUseCase @Inject constructor(
    private val repository: PostsFeedRepository
) {
    operator fun invoke(): Flow<AuthorizationStateResult> {
        return repository.getAuthStatusFlow()
    }
}