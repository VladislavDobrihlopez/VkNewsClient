package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import javax.inject.Inject

class GetPostsWithSpecificTagUseCase @Inject constructor(
    val repository: StoredPostsFeedRepository
) {
    suspend operator fun invoke(itemTags: List<ItemTag>) {
        repository.getPostsWithSpecifiedTags(itemTags)
    }
}