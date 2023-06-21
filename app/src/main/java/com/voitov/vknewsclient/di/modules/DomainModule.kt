package com.voitov.vknewsclient.di.modules

import com.voitov.vknewsclient.data.repositoriesImpl.PostsFeedRepositoryImpl
import com.voitov.vknewsclient.di.scopes.ApplicationScope
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @ApplicationScope
    @Binds
    fun provideRepositoryImplementation(repository: PostsFeedRepositoryImpl): PostsFeedRepository
}