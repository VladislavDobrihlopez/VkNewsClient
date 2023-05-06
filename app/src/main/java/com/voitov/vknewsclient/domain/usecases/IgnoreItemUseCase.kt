package com.voitov.vknewsclient.domain.usecases

import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository

class IgnoreItemUseCase(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(postItem: PostItem) {
        repository.ignoreItem(postItem)
    }
}