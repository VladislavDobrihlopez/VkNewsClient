package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.data.NewsFeedRepository
import com.voitov.vknewsclient.domain.entities.PostItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsFeedRepository(application)

    private val initialScreenState = NewsFeedScreenState.InitialState

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialScreenState)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState

    init {
        _screenState.value = NewsFeedScreenState.LoadingState
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            val newsFeedContent = repository.loadRecommendations()
            delay(500)
            _screenState.value = NewsFeedScreenState.ShowingPostsState(posts = newsFeedContent)
        }
    }

    fun loadContinuingPosts() {
        _screenState.value = NewsFeedScreenState.ShowingPostsState(repository.posts, true)
        loadNews()
    }

    fun changeLikeStatus(post: PostItem) {
        if (!post.isLikedByUser) {
            viewModelScope.launch {
                repository.changeLikeStatus(post)
                _screenState.value = NewsFeedScreenState.ShowingPostsState(posts = repository.posts)
            }
        } else {
            viewModelScope.launch {
                repository.changeLikeStatus(post)
                _screenState.value = NewsFeedScreenState.ShowingPostsState(posts = repository.posts)
            }
        }
    }

    fun ignoreItem(post: PostItem) {
        viewModelScope.launch {
            repository.ignoreItem(post)
            _screenState.value = NewsFeedScreenState.ShowingPostsState(repository.posts)
        }
    }
}