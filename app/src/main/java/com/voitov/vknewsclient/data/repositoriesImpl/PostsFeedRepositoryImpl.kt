package com.voitov.vknewsclient.data.repositoriesImpl

import com.voitov.vknewsclient.data.datasources.PostsFeedRemoteDataSource
import com.voitov.vknewsclient.domain.AuthorizationStateResult
import com.voitov.vknewsclient.domain.CommentsResult
import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PostsFeedRepositoryImpl @Inject constructor(
    private val source: PostsFeedRemoteDataSource
) : PostsFeedRepository {
    override fun getRecommendationsFlow() = source.getRecommendationsFlow()

    override fun getCommentsFlow(post: PostItem) = source.getCommentsFlow(post)

    override fun getAuthStatusFlow() = source.getAuthStatusFlow()

    override suspend fun retrieveNextRecommendations() {
        source.retrieveNextRecommendations()
    }

    override suspend fun retrieveNextChunkOfComments(post: PostItem) {
        source.retrieveNextChunkOfComments(post)
    }

    override suspend fun retrySigningIn() {
        source.retrySigningIn()
    }

    override suspend fun reverseLikeStatus(post: PostItem) {
        source.reverseLikeStatus(post)
    }

    override suspend fun sharePostOnProfileWall(post: PostItem) {
        source.sharePostOnProfileWall(post)
    }

    override suspend fun ignoreItem(post: PostItem) {
        source.ignoreItem(post)
    }
}