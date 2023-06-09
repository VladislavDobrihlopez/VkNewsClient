package com.voitov.vknewsclient.domain.repository

import com.voitov.vknewsclient.domain.ProfileResult
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileDataFlow(userId: String): Flow<ProfileResult>
}