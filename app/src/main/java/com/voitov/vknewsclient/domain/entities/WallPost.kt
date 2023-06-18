package com.voitov.vknewsclient.domain.entities

import androidx.compose.runtime.Immutable
import com.voitov.vknewsclient.domain.SocialMetric

@Immutable
data class WallPost(
    val id: Long,
    val producerId: Long,
    val producerPhotoUrl: String,
    val producerName: String,
    val postType: String,
    val date: String,
    val contentText: String,
    val isLikedByUser: Boolean,
    val isSharedByUser: Boolean,
    val contentImageUrl: List<String>,
    val postLifecycleHierarchy: List<InnerWallPost>,
    val metrics: List<SocialMetric>,
) {


    companion object {
        const val TYPE_POST = "post"
        const val TYPE_REPLY = "reply"
    }

    override fun toString(): String {
        return "WallPost(id=$id, producerPhotoUrl='$producerPhotoUrl', producerName='$producerName', postType='$postType', date='$date', contentText='$contentText', isLikedByUser=$isLikedByUser, contentImageUrl=$contentImageUrl, postLifecycleHierarchy=$postLifecycleHierarchy, metrics=$metrics)"
    }
}