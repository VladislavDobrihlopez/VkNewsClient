package com.voitov.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.PostItem
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.ui.theme.getMetricByType
import kotlin.random.Random

class MainViewModel : ViewModel() {
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

    private val _newsPost = MutableLiveData<List<PostItem>>(fakePosts)
    val newsPost: LiveData<List<PostItem>>
        get() = _newsPost

    private fun checkWhetherIdIsValid(id: Int) {
        if (fakePosts.isEmpty() || (id !in 0 until fakePosts.size)) {
            throw IllegalArgumentException()
        }
    }

    fun updateMetric(postId: Int, metric: SocialMetric) {
        checkWhetherIdIsValid(postId)
        val oldPosts = _newsPost.value ?: throw IllegalStateException()
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

        _newsPost.value = newPosts
    }

    fun remove(postId: Int) {
        val oldPosts = _newsPost.value ?: throw IllegalStateException()
        val newPosts = oldPosts.toMutableList()

        newPosts.remove(newPosts.find { it.id == postId })
        _newsPost.value = newPosts
    }
}