package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.repository.StoredNewsFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNewsUseCase @Inject constructor(
    val repository: StoredNewsFeedRepository
) {
    operator fun invoke(): Flow<List<TaggedPostItem>> {
        return repository.getAllNews()
    }
}