package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): Flow<AuthorizationStateResult> {
        return repository.getAuthStatusFlow()
    }
}