package com.voitov.vknewsclient.domain

import com.voitov.vknewsclient.domain.entities.PostItem

sealed class NewsFeedResult {
    data class Success(val posts: List<PostItem>) : NewsFeedResult()
    object Failure : NewsFeedResult()
    object EndOfNewsFeed : NewsFeedResult()
}
