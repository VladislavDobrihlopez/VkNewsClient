package com.voitov.vknewsclient.presentation.commentsScreen

import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem

sealed class CommentsScreenState {
    object InitialState : CommentsScreenState()
    object LoadingState : CommentsScreenState()

    sealed class DisplayCommentsState(
        open val post: PostItem,
        open val comments: List<PostCommentItem>,
        open val isDataBeingLoaded: Boolean = false
    ): CommentsScreenState() {
        sealed class CachedVersionState(
            override val post: PostItem,
            override val comments: List<PostCommentItem>, // so far retrieved
            override val isDataBeingLoaded: Boolean = false
        ) : DisplayCommentsState(post, comments, isDataBeingLoaded) {
            data class FailureState(
                val ex: Throwable,
                override val post: PostItem,
                override val comments: List<PostCommentItem>,
                override val isDataBeingLoaded: Boolean = false
            ) : CachedVersionState(post, comments, isDataBeingLoaded)

            data class EndOfCommentsState(
                override val post: PostItem,
                override val comments: List<PostCommentItem>,
                override val isDataBeingLoaded: Boolean = false
            ) : CachedVersionState(post, comments, isDataBeingLoaded)
        }

        data class Success(
            override val post: PostItem,
            override val comments: List<PostCommentItem>,
            override val isDataBeingLoaded: Boolean = false
        ) : DisplayCommentsState(post, comments, isDataBeingLoaded)
    }
}
