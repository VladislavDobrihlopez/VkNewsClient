package com.voitov.vknewsclient.domain.entities

import com.voitov.vknewsclient.domain.SocialMetric

data class PostItem(
    val id: Long,
    val communityId: Long,
    val communityPhotoUrl: String,
    val authorName: String,
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