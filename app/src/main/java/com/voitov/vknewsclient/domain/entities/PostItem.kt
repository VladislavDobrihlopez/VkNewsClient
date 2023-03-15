package com.voitov.vknewsclient.domain.entities

import com.voitov.vknewsclient.domain.SocialMetric

data class PostItem(
    val id: String,
    val communityPhotoUrl: String,
    val authorName: String,
    val date: String,
    val contentText: String,
    val isLiked: Boolean,
    val contentImageUrl: String?,
    val metrics: List<SocialMetric>,
)