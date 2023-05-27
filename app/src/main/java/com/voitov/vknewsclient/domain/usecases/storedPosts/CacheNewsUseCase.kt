package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.repository.StoredNewsFeedRepository
import javax.inject.Inject

class CacheNewsUseCase @Inject constructor(
    private val repository: StoredNewsFeedRepository
) {
    suspend operator fun invoke(pieceOfNews: TaggedPostItem) {
        repository.cacheNews(pieceOfNews)
    }
}