package com.voitov.vknewsclient.ui.theme.homeScreen

import com.voitov.vknewsclient.domain.PostCommentItem
import com.voitov.vknewsclient.domain.PostItem

sealed class HomeScreenState {
    object InitialState : HomeScreenState()
    data class NewsFeedState(
        val posts: List<PostItem>
    ) : HomeScreenState()

    data class CommentsState(
        val postId: Int,
        val comments: List<PostCommentItem>
    ) : HomeScreenState()
}
