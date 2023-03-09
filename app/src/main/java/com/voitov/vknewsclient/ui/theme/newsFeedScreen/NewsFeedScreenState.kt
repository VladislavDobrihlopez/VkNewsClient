package com.voitov.vknewsclient.ui.theme.newsFeedScreen

import com.voitov.vknewsclient.domain.PostItem

sealed class NewsFeedScreenState {
    object InitialState : NewsFeedScreenState()
    data class ShowingPostsState(
        val posts: List<PostItem>
    ) : NewsFeedScreenState()
}
