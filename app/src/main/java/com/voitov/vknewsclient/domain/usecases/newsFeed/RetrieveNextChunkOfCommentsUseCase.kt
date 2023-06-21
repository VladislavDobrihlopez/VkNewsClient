package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import javax.inject.Inject

class RetrieveNextChunkOfCommentsUseCase @Inject constructor(
    private val repository: PostsFeedRepository
) {
    suspend operator fun invoke(post: PostItem) {
        repository.retrieveNextChunkOfComments(post)
    }
}