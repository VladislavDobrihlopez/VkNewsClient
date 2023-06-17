package com.voitov.vknewsclient.presentation.commentsScreen

import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem

sealed class CommentsScreenState {
    object InitialState : CommentsScreenState()
    object LoadingState : CommentsScreenState()
    sealed class CachedVersionState(
        open val post: PostItem,
        open val soFarRetrievedComments: List<PostCommentItem>
    ) : CommentsScreenState() {
        data class FailureState(
            val ex: Throwable,
            override val post: PostItem,
            override val soFarRetrievedComments: List<PostCommentItem>,
        ) : CachedVersionState(post, soFarRetrievedComments)

        data class EndOfCommentsState(
            override val post: PostItem,
            override val soFarRetrievedComments: List<PostCommentItem>,
        ) : CachedVersionState(post, soFarRetrievedComments)
    }

    data class DisplayCommentsState(
        val post: PostItem,
        val comments: List<PostCommentItem>,
        val isDataBeingLoaded: Boolean = false
    ) : CommentsScreenState()
}
