package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class SharePostUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(postItem: PostItem) {
        repository.sharePostOnProfileWall(postItem)
    }
}