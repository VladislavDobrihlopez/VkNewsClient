package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import javax.inject.Inject

class SharePostUseCase @Inject constructor(
    private val repository: PostsFeedRepository
) {
    suspend operator fun invoke(postItem: PostItem) {
        repository.sharePostOnProfileWall(postItem)
    }
}