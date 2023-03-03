package com.voitov.vknewsclient.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.PostItem
import com.voitov.vknewsclient.domain.SocialMetric

class MainViewModel : ViewModel() {
    private val _newsPost = MutableLiveData<PostItem>(PostItem())
    val newsPost: LiveData<PostItem>
        get() = _newsPost

    fun updateMetric(metric: SocialMetric) {
        val oldPostInfo = _newsPost.value ?: throw IllegalStateException()
        val oldFeedbackInfo = oldPostInfo.metrics

        val itemIndex = oldFeedbackInfo.indexOf(oldFeedbackInfo.getMetricByType(metric.type))
        val oldItemMetric = oldFeedbackInfo[itemIndex]
        val newItemMetric = oldItemMetric.copy(count = oldItemMetric.count + 1)

        val newFeedbackInfo = oldFeedbackInfo.toMutableList()

        newFeedbackInfo[itemIndex] = newItemMetric
        _newsPost.value = oldPostInfo.copy(metrics = newFeedbackInfo)
    }
}