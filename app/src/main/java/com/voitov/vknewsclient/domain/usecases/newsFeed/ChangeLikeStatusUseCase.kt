package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: PostsFeedRepository
) {
    suspend operator fun invoke(postItem: PostItem) {
        repository.reverseLikeStatus(postItem)
    }
}