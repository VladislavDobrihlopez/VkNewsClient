package com.voitov.vknewsclient.di.modules

import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.presentation.profileScreen.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
interface ProfileScreenModule {
    @IntoMap
    @StringKey("ProfileViewModel")
    @Binds
    fun provideProfileViewModel(viewModel: ProfileViewModel): ViewModel
}