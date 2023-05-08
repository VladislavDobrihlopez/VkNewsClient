package com.voitov.vknewsclient.di.modules

import com.voitov.vknewsclient.data.NewsFeedRepositoryImpl
import com.voitov.vknewsclient.di.scopes.ApplicationScope
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @ApplicationScope
    @Binds
    fun provideRepositoryImplementation(repository: NewsFeedRepositoryImpl): NewsFeedRepository
}