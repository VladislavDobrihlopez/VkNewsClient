package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.domain.NewsFeedResult
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.usecases.newsFeed.ChangeLikeStatusUseCase
import com.voitov.vknewsclient.domain.usecases.newsFeed.GetRecommendationsUseCase
import com.voitov.vknewsclient.domain.usecases.newsFeed.IgnoreItemUseCase
import com.voitov.vknewsclient.domain.usecases.newsFeed.RetrieveNextRecommendationsUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.CacheNewsUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.GetTagsUseCase
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedScreenViewModel @Inject constructor(
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val ignoreItemUseCase: IgnoreItemUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val retrieveNextRecommendationsUseCase: RetrieveNextRecommendationsUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val cacheNewsUseCase: CacheNewsUseCase
) : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("ERROR_TEST", "exception is caught")
    }

    val tagsFlow = getTagsUseCase.invoke()

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

    private val confirmationEvents = MutableSharedFlow<NewsFeedScreenContentState>()

    private val confirmationFlow = flow<NewsFeedScreenContentState> {
        confirmationEvents.collect {
            emit(it)
        }
    }

    val screenContentStateFlow: Flow<NewsFeedScreenContentState> =
        MutableStateFlow<NewsFeedScreenContentState>(NewsFeedScreenContentState.Content)
            .mergeWith(confirmationFlow)

    fun confirmLikeAction(post: PostItem) {
        viewModelScope.launch {
            confirmationEvents.emit(NewsFeedScreenContentState.OnPostLikeActionConfirmation(post))
        }
    }

    fun confirmActionOnSwipeEndToStart(post: PostItem) {
        viewModelScope.launch {
            confirmationEvents.emit(NewsFeedScreenContentState.OnEndToStartActionConfirmation(post = post))
        }
    }

    fun confirmActionOnSwipeStartToEnd(post: PostItem) {
        viewModelScope.launch {
            confirmationEvents.emit(NewsFeedScreenContentState.OnStartToEndActionConfirmation(post = post))
        }
    }

    fun dismiss() {
        viewModelScope.launch {
            confirmationEvents.emit(NewsFeedScreenContentState.Content)
        }
    }

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
                confirmationEvents.emit(NewsFeedScreenContentState.Content)
            }
        } else {
            viewModelScope.launch(exceptionHandler) {
                changeLikeStatusUseCase(post)
                confirmationEvents.emit(NewsFeedScreenContentState.Content)
            }
        }
    }

    fun ignoreItem(post: PostItem) {
        viewModelScope.launch(exceptionHandler) {
            ignoreItemUseCase(post)
            confirmationEvents.emit(NewsFeedScreenContentState.Content)
        }
    }

    fun cachePost(post: PostItem, tag: ItemTag) {
        viewModelScope.launch {
            cacheNewsUseCase.invoke(TaggedPostItem(tag, post))
        }
    }
}