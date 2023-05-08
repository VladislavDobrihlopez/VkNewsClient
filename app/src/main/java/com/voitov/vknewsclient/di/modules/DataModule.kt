package com.voitov.vknewsclient.di.modules

import android.content.Context
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.voitov.vknewsclient.data.NewsFeedRepositoryImpl
import com.voitov.vknewsclient.data.network.ApiFactory
import com.voitov.vknewsclient.data.network.ApiService
import com.voitov.vknewsclient.di.scopes.ApplicationScope
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @ApplicationScope
    @Binds
    fun provideRepositoryImplementation(
        repository: NewsFeedRepositoryImpl
    ): NewsFeedRepository


    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideVKStorage(
            context: Context
        ): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(context)
        }
    }
}