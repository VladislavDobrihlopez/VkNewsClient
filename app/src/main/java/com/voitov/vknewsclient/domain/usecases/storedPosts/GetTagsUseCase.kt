package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(
    private val repository: StoredPostsFeedRepository
) {
    operator fun invoke(): Flow<List<ItemTag>> {
        return repository.getAllTags()
    }
}