package com.voitov.vknewsclient.domain.repository

import com.voitov.vknewsclient.domain.entities.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileFlow(userId: String): Flow<Profile>
}