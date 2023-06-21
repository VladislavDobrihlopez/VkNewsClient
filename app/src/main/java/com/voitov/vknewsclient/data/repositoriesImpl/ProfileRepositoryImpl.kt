package com.voitov.vknewsclient.data.repositoriesImpl

import com.voitov.vknewsclient.data.datasources.ProfileRemoteDataSource
import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val source: ProfileRemoteDataSource
) : ProfileRepository {
    override fun getProfileDataFlow(author: ProfileAuthor) = source.getProfileDataFlow(author)

    override suspend fun retrieveNextChunkOfWallPosts() {
        source.retrieveNextChunkOfWallPosts()
    }
}