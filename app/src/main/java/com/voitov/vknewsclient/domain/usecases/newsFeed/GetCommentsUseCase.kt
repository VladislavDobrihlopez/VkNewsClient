package com.voitov.vknewsclient.domain.usecases.newsFeed

import com.voitov.vknewsclient.domain.CommentsResult
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: PostsFeedRepository
) {
    operator fun invoke(post: PostItem): SharedFlow<CommentsResult> {
        return repository.getCommentsFlow(post)
    }
}