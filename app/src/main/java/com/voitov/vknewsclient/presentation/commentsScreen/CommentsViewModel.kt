package com.voitov.vknewsclient.presentation.commentsScreen

import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val post: PostItem
) : ViewModel() {
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