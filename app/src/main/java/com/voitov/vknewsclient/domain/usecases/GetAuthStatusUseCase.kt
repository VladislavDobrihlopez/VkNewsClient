package com.voitov.vknewsclient.domain.usecases

import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.Flow

class GetAuthStatusUseCase(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): Flow<AuthorizationStateResult> {
        return repository.getAuthStatusFlow()
    }
}