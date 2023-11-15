package com.voitov.vknewsclient.domain

import com.voitov.vknewsclient.domain.entities.PostCommentItem

sealed class CommentsResult {
    data class Success(val comments: List<PostCommentItem>) : CommentsResult()
    data class Failure(val ex: Throwable) : CommentsResult()
    object Initial : CommentsResult()
    object EndOfComments : CommentsResult()
}
