package com.voitov.vknewsclient.domain.usecases

import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.Flow

class GetCommentsUseCase(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(post: PostItem): Flow<List<PostCommentItem>> {
        return repository.getCommentsFlow(post)
    }
}