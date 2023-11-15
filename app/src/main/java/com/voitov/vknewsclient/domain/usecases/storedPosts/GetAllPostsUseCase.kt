package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(
    val repository: StoredPostsFeedRepository
) {
    operator fun invoke(): StateFlow<List<TaggedPostItem>> {
        return repository.getAllPosts()
    }
}