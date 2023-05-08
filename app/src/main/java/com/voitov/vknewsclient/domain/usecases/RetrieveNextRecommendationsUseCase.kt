package com.voitov.vknewsclient.domain.usecases

import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class RetrieveNextRecommendationsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke() {
        repository.retrieveNextRecommendations()
    }
}