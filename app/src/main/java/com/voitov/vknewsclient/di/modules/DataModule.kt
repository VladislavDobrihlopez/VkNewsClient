package com.voitov.vknewsclient.di.modules

import android.content.Context
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.voitov.vknewsclient.data.database.dao.TaggedFeedPostsDao
import com.voitov.vknewsclient.data.database.dao.TagsDao
import com.voitov.vknewsclient.data.database.factories.PostsDatabase
import com.voitov.vknewsclient.data.datasources.LocalDataSource
import com.voitov.vknewsclient.data.datasources.LocalDataSourceImpl
import com.voitov.vknewsclient.data.datasources.PostsFeedRemoteDataSource
import com.voitov.vknewsclient.data.datasources.PostsFeedRemoteDataSourceImpl
import com.voitov.vknewsclient.data.datasources.ProfileRemoteDataSource
import com.voitov.vknewsclient.data.datasources.ProfileRemoteDataSourceImpl
import com.voitov.vknewsclient.data.network.ApiFactory
import com.voitov.vknewsclient.data.network.ProfileApiService
import com.voitov.vknewsclient.data.network.RecommendationsFeedApiService
import com.voitov.vknewsclient.data.repositoriesImpl.PostsFeedRepositoryImpl
import com.voitov.vknewsclient.data.repositoriesImpl.ProfileRepositoryImpl
import com.voitov.vknewsclient.data.repositoriesImpl.StoredPostsFeedRepositoryImpl
import com.voitov.vknewsclient.di.scopes.ApplicationScope
import com.voitov.vknewsclient.domain.repository.PostsFeedRepository
import com.voitov.vknewsclient.domain.repository.ProfileRepository
import com.voitov.vknewsclient.domain.repository.StoredPostsFeedRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @ApplicationScope
    @Binds
    fun provideNewsFeedRepositoryImplementation(
        repository: PostsFeedRepositoryImpl
    ): PostsFeedRepository

    @ApplicationScope
    @Binds
    fun provideStoredNewsFeedRepositoryImplementation(
        repository: StoredPostsFeedRepositoryImpl
    ): StoredPostsFeedRepository

    @ApplicationScope
    @Binds
    fun provideProfileRepository(
        repository: ProfileRepositoryImpl
    ): ProfileRepository

    @ApplicationScope
    @Binds
    fun provideLocalDataSource(source: LocalDataSourceImpl): LocalDataSource

    @ApplicationScope
    @Binds
    fun providePostsFeedRemoteDataSource(source: PostsFeedRemoteDataSourceImpl): PostsFeedRemoteDataSource

    @ApplicationScope
    @Binds
    fun provideProfileRemoteDataSource(source: ProfileRemoteDataSourceImpl): ProfileRemoteDataSource

    companion object {
        @ApplicationScope
        @Provides
        fun providePostFeedApiService(): RecommendationsFeedApiService {
            return ApiFactory.recommendationsFeedApiService
        }

        @ApplicationScope
        @Provides
        fun provideProfileApiService(): ProfileApiService {
            return ApiFactory.profileApiService
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