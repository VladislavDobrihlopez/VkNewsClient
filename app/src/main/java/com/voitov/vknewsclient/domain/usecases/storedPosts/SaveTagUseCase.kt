package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import javax.inject.Inject

class SaveTagUseCase @Inject constructor(
    private val repository: StoredPostsFeedRepository
) {
    suspend operator fun invoke(itemTag: ItemTag) {
        repository.saveTag(itemTag)
    }
}