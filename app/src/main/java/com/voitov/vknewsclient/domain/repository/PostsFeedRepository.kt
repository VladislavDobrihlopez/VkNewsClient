package com.voitov.vknewsclient.domain.repository

import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.CommentsResult
import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.entities.PostItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface PostsFeedRepository {
    fun getRecommendationsFlow(): StateFlow<NewsFeedResult>

    fun getCommentsFlow(post: PostItem): SharedFlow<CommentsResult>

    fun getAuthStatusFlow(): Flow<AuthorizationStateResult>

    suspend fun retrieveNextRecommendations()

    suspend fun retrieveNextChunkOfComments(post: PostItem)

    suspend fun retrySigningIn()

    suspend fun reverseLikeStatus(post: PostItem)
    suspend fun sharePostOnProfileWall(post: PostItem)

    suspend fun ignoreItem(post: PostItem)
}