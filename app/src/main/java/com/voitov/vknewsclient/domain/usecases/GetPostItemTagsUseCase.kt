package com.voitov.vknewsclient.domain.usecases

import com.voitov.vknewsclient.domain.entities.PostItemTag
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostItemTagsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): Flow<List<PostItemTag>> {
        return repository.getPostTags()
    }
}