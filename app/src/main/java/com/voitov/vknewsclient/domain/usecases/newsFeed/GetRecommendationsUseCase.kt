package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val repository: PostsFeedRepository
) {
    operator fun invoke(): StateFlow<NewsFeedResult> {
        return repository.getRecommendationsFlow()
    }
}