package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.data.NewsFeedRepository
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsFeedRepository(application)

    private val screenStateFlow: StateFlow<List<PostItem>> = repository.recommendations

    private val nextPostsEvent = MutableSharedFlow<Unit>()

    private val nextPostsFlow = flow<NewsFeedScreenState> {
        nextPostsEvent.collect {
            emit(NewsFeedScreenState.ShowingPostsState(screenStateFlow.value, true))
        }
    }

    val screenState: Flow<NewsFeedScreenState> = screenStateFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.ShowingPostsState(it, false) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.LoadingState) }
        .mergeWith(nextPostsFlow)

    fun loadContinuingPosts() {
        viewModelScope.launch {
            nextPostsEvent.emit(Unit)
            repository.retrieveNextRecommendations()
        }
    }

    fun changeLikeStatus(post: PostItem) {
        if (!post.isLikedByUser) {
            viewModelScope.launch {
                repository.changeLikeStatus(post)
            }
        } else {
            viewModelScope.launch {
                repository.changeLikeStatus(post)
            }
        }
    }

    fun ignoreItem(post: PostItem) {
        viewModelScope.launch {
            repository.ignoreItem(post)
        }
    }
}