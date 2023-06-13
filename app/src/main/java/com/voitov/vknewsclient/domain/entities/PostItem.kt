package com.voitov.vknewsclient.domain.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import com.voitov.vknewsclient.domain.SocialMetric

@Immutable
data class PostItem(
    val id: Long,
    val communityId: Long,
    val communityPhotoUrl: String,
    val authorName: String,
    val dateInMillis: Long,
    val date: String,
    val contentText: String,
    val isLikedByUser: Boolean,
    val contentImageUrl: List<String>,
    val metrics: List<SocialMetric>,
) {
    companion object {
        const val TYPE = "post"
    }
}