package com.voitov.vknewsclient.ui.theme.commentsScreen

import com.voitov.vknewsclient.domain.PostCommentItem

sealed class CommentsScreenState {
    object InitialState : CommentsScreenState()
    data class ShowingCommentsState(
        val postId: Int,
        val comments: List<PostCommentItem>
    ) : CommentsScreenState()
}
