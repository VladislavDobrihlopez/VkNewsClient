package com.voitov.vknewsclient.presentation.commentsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.data.NewsFeedRepository
import com.voitov.vknewsclient.domain.entities.PostItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.delayEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CommentsViewModel(
    application: Application,
    private val post: PostItem
) : AndroidViewModel(application) {
    private val repository = NewsFeedRepository(application)
    val screenState: Flow<CommentsScreenState> = repository.loadComments(post)
        .map {
            CommentsScreenState.ShowingCommentsState(
                post = post,
                comments = it
            ) as CommentsScreenState
        }
        .onStart { emit(CommentsScreenState.LoadingState) }
        .onEach { delay(500) }
}