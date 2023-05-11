package com.voitov.vknewsclient.di.modules

import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.presentation.mainScreen.AuthorizationViewModel
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
interface ViewModelsModule {
    @IntoMap
    @StringKey("AuthorizationViewModel")
    @Binds
    fun provideAuthorizationViewModel(viewModel: AuthorizationViewModel): ViewModel

    @IntoMap
    @StringKey("NewsFeedScreenViewModel")
    @Binds
    fun provideNewsFeedViewModel(viewModel: NewsFeedScreenViewModel): ViewModel
}