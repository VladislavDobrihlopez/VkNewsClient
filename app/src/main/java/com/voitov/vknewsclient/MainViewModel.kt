package com.voitov.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.PostCommentItem
import com.voitov.vknewsclient.domain.PostItem
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.ui.theme.homeScreen.HomeScreenState
import com.voitov.vknewsclient.ui.theme.homeScreen.getMetricByType
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val fakePostComments = mutableListOf<PostCommentItem>().apply {
        repeat(25) {
            add(
                PostCommentItem(id = it, authorId = it, postId = it, text = "Some text")
            )
        }
    }

    private val fakePosts = mutableListOf<PostItem>().apply {
        repeat(25) {
            add(
                PostItem(
                    id = it,
                    metrics = listOf(
                        SocialMetric(MetricsType.COMMENTS, Random.nextInt(0, 1000)),
                        SocialMetric(MetricsType.VIEWS, Random.nextInt(0, 1000)),
                        SocialMetric(MetricsType.SHARES, Random.nextInt(0, 1000)),
                        SocialMetric(MetricsType.LIKES, Random.nextInt(0, 1000))
                    )
                )
            )
        }
    }

    private val initialScreenState = HomeScreenState.NewsFeedState(fakePosts)

    private val _screenState = MutableLiveData<HomeScreenState>(initialScreenState)
    val screenState: LiveData<HomeScreenState>
        get() = _screenState

    private var savedNewsFeedState: HomeScreenState? = initialScreenState

    private fun checkWhetherIdIsValid(id: Int) {
        if (fakePosts.isEmpty() || (id !in 0 until fakePosts.size)) {
            throw IllegalArgumentException()
        }
    }

    fun displayComments(postId: Int) {
        savedNewsFeedState = _screenState.value
        _screenState.value = HomeScreenState.CommentsState(
            postId = postId,
            comments = fakePostComments
        )
    }

    fun restorePreviousState() {
        _screenState.value = savedNewsFeedState
    }

    fun updateMetric(postId: Int, metric: SocialMetric) {
        val currentState = _screenState.value
        if (currentState !is HomeScreenState.NewsFeedState) {
            return
        }

        checkWhetherIdIsValid(postId)

        val oldPosts = currentState.posts
        val oldPostInfo = oldPosts.find { it.id == postId } ?: throw IllegalStateException()
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

        _screenState.value = HomeScreenState.NewsFeedState(newPosts)
    }

    fun remove(postId: Int) {
        val currentState = _screenState.value
        if (currentState !is HomeScreenState.NewsFeedState) {
            return
        }

        val oldPosts = currentState.posts
        val newPosts = oldPosts.toMutableList()

        newPosts.remove(newPosts.find { it.id == postId })
        _screenState.value = HomeScreenState.NewsFeedState(newPosts)
    }
}