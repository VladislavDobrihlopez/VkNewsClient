package com.voitov.vknewsclient.di.components

import com.voitov.vknewsclient.di.modules.ProfileScreenModule
import com.voitov.vknewsclient.di.scopes.ProfileScreenScope
import com.voitov.vknewsclient.domain.usecases.profile.ProfileAuthor
import com.voitov.vknewsclient.presentation.ViewModelsFactory
import dagger.BindsInstance
import dagger.Subcomponent

@ProfileScreenScope
@Subcomponent(modules = [ProfileScreenModule::class])
interface ProfileScreenComponent {
    fun getViewModelsFactory(): ViewModelsFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance profileAuthor: ProfileAuthor
        ): ProfileScreenComponent
    }
}