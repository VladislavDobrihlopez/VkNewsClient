package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.voitov.vknewsclient.domain.usecases.GetRecommendationsUseCase
import com.voitov.vknewsclient.domain.usecases.IgnoreItemUseCase
import com.voitov.vknewsclient.domain.usecases.RetrieveNextRecommendationsUseCase
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val ignoreItemUseCase: IgnoreItemUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val retrieveNextRecommendationsUseCase: RetrieveNextRecommendationsUseCase
) : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("ERROR_TEST", "exception is caught")
    }

    private var previousPosts: List<PostItem> = listOf()
    private val screenStateFlow: StateFlow<NewsFeedResult> = getRecommendationsUseCase()

    private val nextPostsEvent = MutableSharedFlow<Unit>()

    private val nextPostsFlow = flow<NewsFeedScreenState> {
        nextPostsEvent.collect {
            emit(NewsFeedScreenState.ShowingPostsState(previousPosts, true))
        }
    }

    val screenState: Flow<NewsFeedScreenState> = screenStateFlow
        .map {
            Log.d("INTERNET_TEST", "viewmodel map ${it::class.simpleName}")

            when (val feedLoadResult = screenStateFlow.value) {
                is NewsFeedResult.Failure -> NewsFeedScreenState.ErrorState
                is NewsFeedResult.Success -> NewsFeedScreenState.ShowingPostsState(
                    posts = feedLoadResult.posts,
                    isDataBeingLoaded = false
                )
                else -> throw IllegalStateException("Unexpected type: $feedLoadResult")
            } as NewsFeedScreenState
        }
        .filter {
            when (val feedScreenState = it) {
                is NewsFeedScreenState.ShowingPostsState -> {
                    val result = feedScreenState.posts.isNotEmpty()
                    if (result) {
                        previousPosts = feedScreenState.posts.toList()
                    }
                    result
                }

                else -> true
            }
        }
        .onStart { emit(NewsFeedScreenState.LoadingState) }
        .mergeWith(nextPostsFlow)

    fun loadContinuingPosts() {
        viewModelScope.launch {
            Log.d("INTERNET_TEST", "trying loading new data")
            retrieveNextRecommendationsUseCase()
            nextPostsEvent.emit(Unit)
        }
    }

    fun changeLikeStatus(post: PostItem) {
        if (!post.isLikedByUser) {
            viewModelScope.launch(exceptionHandler) {
                changeLikeStatusUseCase(post)
            }
        } else {
            viewModelScope.launch(exceptionHandler) {
                changeLikeStatusUseCase(post)
            }
        }
    }

    fun ignoreItem(post: PostItem) {
        viewModelScope.launch(exceptionHandler) {
            ignoreItemUseCase(post)
        }
    }
}