package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import javax.inject.Inject

class CachePostUseCase @Inject constructor(
    private val repository: StoredPostsFeedRepository
) {
    suspend operator fun invoke(pieceOfNews: TaggedPostItem) {
        repository.cachePosts(pieceOfNews)
    }
}