package com.voitov.vknewsclient.di.modules

import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.presentation.favoritePostsScreen.FavoritesViewModel
import com.voitov.vknewsclient.presentation.mainScreen.AuthorizationViewModel
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreenViewModel
import com.voitov.vknewsclient.presentation.profileScreen.ProfileViewModel
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

    @IntoMap
    @StringKey("FavoritesViewModel")
    @Binds
    fun provideFavoritesViewModel(viewModel: FavoritesViewModel): ViewModel

    @IntoMap
    @StringKey("ProfileViewModel")
    @Binds
    fun provideProfileViewModel(viewModel: ProfileViewModel): ViewModel
}