package com.voitov.vknewsclient.presentation.commentsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.domain.CommentsResult
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.usecases.newsFeed.GetCommentsUseCase
import com.voitov.vknewsclient.domain.usecases.newsFeed.RetrieveNextChunkOfCommentsUseCase
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val retrieveNextChunkOfCommentsUseCase: RetrieveNextChunkOfCommentsUseCase,
    private val post: PostItem
) : ViewModel() {
    private var viewedAllPosts = false
    private lateinit var cachedData: List<PostCommentItem>
    private val eventContainer = MutableSharedFlow<Unit>()
    private val cachedDataFlow = flow {
        eventContainer.collect {
            emit(
                CommentsScreenState.DisplayCommentsState.Success(
                    post = post,
                    comments = cachedData,
                    isDataBeingLoaded = !viewedAllPosts
                )
            )
        }
    }
    val screenState: Flow<CommentsScreenState> = getCommentsUseCase(post)
        .map { result ->
            Log.d("TEST_COMMENTS_SCREEN", "viewmodel: ${result}")

            when (result) {
                CommentsResult.EndOfComments -> {
                    viewedAllPosts = true
                    CommentsScreenState.DisplayCommentsState.CachedVersionState.EndOfCommentsState(
                        post,
                        cachedData
                    )
                }

                is CommentsResult.Failure -> {
                    CommentsScreenState.DisplayCommentsState.CachedVersionState.FailureState(
                        result.ex,
                        post,
                        cachedData
                    )
                }

                CommentsResult.Initial -> {
                    CommentsScreenState.LoadingState
                }

                CommentsResult.Loading -> {
                    CommentsScreenState.LoadingState
                }

                is CommentsResult.Success -> {
                    Log.d("TEST_COMMENTS_SCREEN", "viewmodel ${result.comments.count()}")

                    CommentsScreenState.DisplayCommentsState.Success(
                        post = post,
                        comments = result.comments
                    )
                }
            }
        }
        .filter {
            when (val commentsState = it) {
                is CommentsScreenState.DisplayCommentsState.Success -> {
                    val result = commentsState.comments.isNotEmpty()
                    if (result) {
                        cachedData = commentsState.comments.toList()
                    }
                    result
                }

                else -> true
            }
        }
        .onStart {
            emit(CommentsScreenState.LoadingState)
            delay(700)
        }
        .mergeWith(cachedDataFlow)

    fun loadCommentsContinuation() {
        viewModelScope.launch {
            Log.d("TEST_COMMENTS_SCREEN", "load comments continuation")
            if (!viewedAllPosts) {
                eventContainer.emit(Unit)
            }
            delay(3000)
            //_screenState.value = CommentsScreenState.LoadingNewChunkOfComments
            retrieveNextChunkOfCommentsUseCase(post)
        }
    }

    companion object {
        private const val DELAY_IN_MILLIS = 150L
    }
}