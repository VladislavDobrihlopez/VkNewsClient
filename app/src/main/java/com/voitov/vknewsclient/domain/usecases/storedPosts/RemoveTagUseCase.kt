package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.repository.StoredNewsFeedRepository
import javax.inject.Inject

class RemoveTagUseCase @Inject constructor(
    private val repository: StoredNewsFeedRepository
) {
    suspend operator fun invoke(itemTag: ItemTag) {
        repository.removeTag(itemTag)
    }
}