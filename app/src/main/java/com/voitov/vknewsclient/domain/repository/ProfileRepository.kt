package com.voitov.vknewsclient.domain.repository

import com.voitov.vknewsclient.domain.ProfileResult
import com.voitov.vknewsclient.domain.ProfileAuthor
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileDataFlow(author: ProfileAuthor): Flow<ProfileResult>
}