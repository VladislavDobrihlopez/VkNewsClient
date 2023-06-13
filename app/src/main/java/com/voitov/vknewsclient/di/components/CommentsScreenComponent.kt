package com.voitov.vknewsclient.di.components

import com.voitov.vknewsclient.di.modules.CommentScreenModule
import com.voitov.vknewsclient.di.scopes.CommentsScreenScope
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.presentation.ViewModelsFactory
import dagger.BindsInstance
import dagger.Subcomponent

@CommentsScreenScope
@Subcomponent(modules = [CommentScreenModule::class])
interface CommentsScreenComponent {
    fun getViewModelsFactory(): ViewModelsFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance postItem: PostItem
        ): CommentsScreenComponent
    }
}