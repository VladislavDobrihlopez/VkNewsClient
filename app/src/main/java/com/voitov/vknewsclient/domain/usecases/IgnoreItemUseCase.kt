package com.voitov.vknewsclient.domain.usecases

import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class IgnoreItemUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(postItem: PostItem) {
        repository.ignoreItem(postItem)
    }
}