package com.voitov.vknewsclient.domain.usecases

import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetRecommendationsUseCase(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<NewsFeedResult> {
        return repository.getRecommendationsFlow()
    }
}