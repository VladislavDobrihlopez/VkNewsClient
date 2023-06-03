package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import com.voitov.vknewsclient.domain.repository.StoredNewsFeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(
    private val repository: StoredNewsFeedRepository
) {
    operator fun invoke(): Flow<List<ItemTag>> {
        return repository.getAllTags()
    }
}