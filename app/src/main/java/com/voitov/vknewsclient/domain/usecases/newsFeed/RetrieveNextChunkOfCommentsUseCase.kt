package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class RetrieveNextChunkOfCommentsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(post: PostItem) {
        repository.retrieveNextChunkOfComments(post)
    }
}