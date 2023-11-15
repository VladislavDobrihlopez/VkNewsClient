package com.voitov.vknewsclient.di.modules

import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.presentation.commentsScreen.CommentsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
interface CommentScreenModule {
    @IntoMap
    @StringKey("CommentsViewModel")
    @Binds
    fun provideCommentsViewModel(viewModel: CommentsViewModel): ViewModel
}