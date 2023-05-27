package com.voitov.vknewsclient.domain.usecases.storedPosts

import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.repository.StoredNewsFeedRepository
import javax.inject.Inject

class GetNewsWithSpecificTagUseCase @Inject constructor(
    val repository: StoredNewsFeedRepository
) {
    operator fun invoke(itemTag: ItemTag) {

    }
}