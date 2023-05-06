package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.data.NewsFeedRepositoryImpl
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

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsFeedRepositoryImpl(application)
    private val changeLikeStatusUseCase = ChangeLikeStatusUseCase(repository)
    private val ignoreItemUseCase = IgnoreItemUseCase(repository)
    private val getRecommendationsUseCase = GetRecommendationsUseCase(repository)
    private val retrieveNextRecommendationsUseCase = RetrieveNextRecommendationsUseCase(repository)
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
            nextPostsEvent.emit(Unit)
            Log.d("ERROR_TEST", "trying loading new data")
            retrieveNextRecommendationsUseCase()
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