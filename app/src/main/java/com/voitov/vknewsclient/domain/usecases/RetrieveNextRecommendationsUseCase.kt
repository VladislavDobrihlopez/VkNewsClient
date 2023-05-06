package com.voitov.vknewsclient.domain.usecases

import com.voitov.vknewsclient.domain.repository.NewsFeedRepository

class RetrieveNextRecommendationsUseCase(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke() {
        repository.retrieveNextRecommendations()
    }
}