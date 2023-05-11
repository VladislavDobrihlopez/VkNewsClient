package com.voitov.vknewsclient.domain.repository

import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.entities.PostItemTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {
    fun getRecommendationsFlow(): StateFlow<NewsFeedResult>

    fun getCommentsFlow(post: PostItem): Flow<List<PostCommentItem>>

    fun getPostTags(): Flow<List<PostItemTag>>

    fun getAuthStatusFlow(): Flow<AuthorizationStateResult>

    suspend fun retrieveNextRecommendations()

    suspend fun retrySigningIn()

    suspend fun changeLikeStatus(post: PostItem)

    suspend fun ignoreItem(post: PostItem)
}