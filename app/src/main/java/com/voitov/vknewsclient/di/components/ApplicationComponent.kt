package com.voitov.vknewsclient.di.components

import android.content.Context
import com.voitov.vknewsclient.di.modules.DataModule
import com.voitov.vknewsclient.di.modules.ViewModelsModule
import com.voitov.vknewsclient.di.scopes.ApplicationScope
import com.voitov.vknewsclient.presentation.ViewModelsFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelsModule::class
    ]
)
interface ApplicationComponent {
    fun getViewModelsFactory(): ViewModelsFactory
    fun getCommentsScreenComponentFactory(): CommentsScreenComponent.Factory
    fun getProfileScreenComponentFactory(): ProfileScreenComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}