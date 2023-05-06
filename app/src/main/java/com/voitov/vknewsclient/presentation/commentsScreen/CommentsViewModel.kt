package com.voitov.vknewsclient.presentation.commentsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.voitov.vknewsclient.data.NewsFeedRepositoryImpl
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class CommentsViewModel(
    application: Application,
    private val post: PostItem
) : AndroidViewModel(application) {
    private val repository = NewsFeedRepositoryImpl(application)
    private val getCommentsUseCase = GetCommentsUseCase(repository)
    val screenState: Flow<CommentsScreenState> = getCommentsUseCase(post)
        .map {
            CommentsScreenState.ShowingCommentsState(
                post = post,
                comments = it
            ) as CommentsScreenState
        }
        .onStart { emit(CommentsScreenState.LoadingState) }
        .onEach { delay(500) }
}