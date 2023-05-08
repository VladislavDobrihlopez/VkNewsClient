package com.voitov.vknewsclient.presentation.commentsScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voitov.vknewsclient.domain.entities.PostItem

class CommentsViewModelFactory(
    private val application: Application,
    private val post: PostItem
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
//            return CommentsViewModel(application, post) as T
//        }
        throw RuntimeException("Unknown viewModel: $modelClass")
    }
}