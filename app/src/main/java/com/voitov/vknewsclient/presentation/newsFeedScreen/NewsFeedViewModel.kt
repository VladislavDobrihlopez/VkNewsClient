package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.data.NewsFeedRepository
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostItem
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val fakePosts = mutableListOf<PostItem>().apply {
//        repeat(25) {
//            add(
//                PostItem(
//                    id = it.toString(),
//                    metrics = listOf(
//                        SocialMetric(MetricsType.COMMENTS, Random.nextInt(0, 1000)),
//                        SocialMetric(MetricsType.VIEWS, Random.nextInt(0, 1000)),
//                        SocialMetric(MetricsType.SHARES, Random.nextInt(0, 1000)),
//                        SocialMetric(MetricsType.LIKES, Random.nextInt(0, 1000))
//                    )
//                )
//            )
//        }
    }

    private val repository = NewsFeedRepository(application)

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            val newsFeedContent = repository.loadRecommendations()
            _screenState.value = NewsFeedScreenState.ShowingPostsState(posts = newsFeedContent)
        }
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

    private val initialScreenState = NewsFeedScreenState.InitialState

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialScreenState)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState

    private var savedNewsFeedState: NewsFeedScreenState? = initialScreenState

    private fun checkWhetherIdIsValid(id: Int) {
        if (fakePosts.isEmpty() || (id !in 0 until fakePosts.size)) {
            throw IllegalArgumentException()
        }
    }

    fun restorePreviousState() {
        _screenState.value = savedNewsFeedState
    }

    fun updateMetric(postId: Int, metric: SocialMetric) {
        val currentState = _screenState.value
        if (currentState !is NewsFeedScreenState.ShowingPostsState) {
            return
        }

        checkWhetherIdIsValid(postId)

        val oldPosts = currentState.posts
        val oldPostInfo = oldPosts.find { it.id.toInt() == postId } ?: throw IllegalStateException()
        val oldPostIndex = oldPosts.indexOf(oldPostInfo)

        val oldFeedbackInfo = oldPostInfo.metrics
        val itemIndex = oldFeedbackInfo.indexOf(oldFeedbackInfo.getMetricByType(metric.type))
        val oldItemMetric = oldFeedbackInfo[itemIndex]
        val newItemMetric = oldItemMetric.copy(count = oldItemMetric.count + 1)

        val newFeedbackInfo = oldFeedbackInfo.toMutableList()
        newFeedbackInfo[itemIndex] = newItemMetric

        val newPosts = oldPosts.toMutableList()
        val newPost = newPosts[oldPostIndex].copy(metrics = newFeedbackInfo)
        newPosts[oldPostIndex] = newPost

        _screenState.value = NewsFeedScreenState.ShowingPostsState(newPosts)
    }

    fun remove(postId: Int) {
        val currentState = _screenState.value
        if (currentState !is NewsFeedScreenState.ShowingPostsState) {
            return
        }

        val oldPosts = currentState.posts
        val newPosts = oldPosts.toMutableList()

        newPosts.remove(newPosts.find { it.id.toInt() == postId })
        _screenState.value = NewsFeedScreenState.ShowingPostsState(newPosts)
    }
}