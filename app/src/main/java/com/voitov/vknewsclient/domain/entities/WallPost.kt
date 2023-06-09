package com.voitov.vknewsclient.domain.entities

import androidx.compose.runtime.Immutable
import com.voitov.vknewsclient.domain.SocialMetric

@Immutable
data class WallPost(
    val id: Long,
    val date: String,
    val contentText: String,
    val isLikedByUser: Boolean,
    val contentImageUrl: String?,
    val metrics: List<SocialMetric>,
) {
    companion object {
        const val TYPE = "post"
    }
}