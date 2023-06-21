package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import javax.inject.Inject

class RemoveCachedPostUseCase @Inject constructor(
    private val repository: StoredPostsFeedRepository
) {
    suspend operator fun invoke(postId: Long) {
        repository.removeCachedPost(postId)
    }
}