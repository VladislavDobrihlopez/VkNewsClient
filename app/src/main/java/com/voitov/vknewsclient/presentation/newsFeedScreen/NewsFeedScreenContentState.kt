package com.voitov.vknewsclient.presentation.newsFeedScreen

import com.voitov.vknewsclient.domain.entities.PostItem

sealed class NewsFeedScreenContentState {
    object Content : NewsFeedScreenContentState()
    data class OnPostLikeActionConfirmation(val post: PostItem) : NewsFeedScreenContentState()
    data class OnPostShareActionConfirmation(val post: PostItem) : NewsFeedScreenContentState()
    data class OnEndToStartActionConfirmation(val post: PostItem) : NewsFeedScreenContentState()
    data class OnStartToEndActionConfirmation(val post: PostItem) : NewsFeedScreenContentState()
}