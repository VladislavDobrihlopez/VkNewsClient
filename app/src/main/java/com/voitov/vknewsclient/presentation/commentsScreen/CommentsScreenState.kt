package com.voitov.vknewsclient.presentation.commentsScreen

import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem

sealed class CommentsScreenState {
    object InitialState : CommentsScreenState()
    object LoadingState : CommentsScreenState()
    data class ShowingCommentsState(
        val post: PostItem,
        val comments: List<PostCommentItem>
    ) : CommentsScreenState()
}
