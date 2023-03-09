package com.voitov.vknewsclient.ui.theme.commentsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommentsViewModelFactory(
    private val postId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            return CommentsViewModel(postId) as T
        }
        throw RuntimeException("Unknown viewModel: $modelClass")
    }
}