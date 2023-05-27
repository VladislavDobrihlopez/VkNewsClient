package com.voitov.vknewsclient.di.modules

import android.content.Context
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.voitov.vknewsclient.data.database.dao.TaggedFeedPostsDao
import com.voitov.vknewsclient.data.database.dao.TagsDao
import com.voitov.vknewsclient.data.database.factories.PostsDatabase
import com.voitov.vknewsclient.data.network.ApiFactory
import com.voitov.vknewsclient.data.network.ApiService
import com.voitov.vknewsclient.data.repositoriesImpl.NewsFeedRepositoryImpl
import com.voitov.vknewsclient.data.repositoriesImpl.StoredNewsFeedRepositoryImpl
import com.voitov.vknewsclient.di.scopes.ApplicationScope
import com.voitov.vknewsclient.domain.repository.NewsFeedRepository
import com.voitov.vknewsclient.domain.repository.StoredNewsFeedRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @ApplicationScope
    @Binds
    fun provideNewsFeedRepositoryImplementation(
        repository: NewsFeedRepositoryImpl
    ): NewsFeedRepository

    @ApplicationScope
    @Binds
    fun provideStoredNewsFeedRepositoryImplementation(
        repository: StoredNewsFeedRepositoryImpl
    ): StoredNewsFeedRepository

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

        @ApplicationScope
        @Provides
        fun provideTaggedFeedPostsDao(context: Context): TaggedFeedPostsDao {
            return PostsDatabase.getInstance(context).getTaggedPostsDao()
        }

        @ApplicationScope
        @Provides
        fun provideTagsDao(context: Context): TagsDao {
            return PostsDatabase.getInstance(context).getTagsDao()
        }
    }
}