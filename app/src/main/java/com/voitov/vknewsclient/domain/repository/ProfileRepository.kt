package com.voitov.vknewsclient.domain.repository

import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.ProfileResult
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    fun getProfileDataFlow(author: ProfileAuthor): SharedFlow<ProfileResult>
    suspend fun retrieveNextChunkOfWallPosts()
}