package com.voitov.vknewsclient.data.datasources

import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.ProfileResult
import kotlinx.coroutines.flow.SharedFlow

interface ProfileRemoteDataSource {
    fun getProfileDataFlow(author: ProfileAuthor): SharedFlow<ProfileResult>
    suspend fun retrieveNextChunkOfWallPosts()
}