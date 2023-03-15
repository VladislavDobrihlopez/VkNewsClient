package com.voitov.vknewsclient.presentation.commentsScreen

import com.voitov.vknewsclient.domain.PostCommentItem

sealed class CommentsScreenState {
    object InitialState : CommentsScreenState()
    data class ShowingCommentsState(
        val postId: Int,
        val comments: List<PostCommentItem>
    ) : CommentsScreenState()
}
