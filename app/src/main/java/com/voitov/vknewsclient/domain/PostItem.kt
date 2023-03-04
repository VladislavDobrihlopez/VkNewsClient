package com.voitov.vknewsclient.domain

import com.voitov.vknewsclient.R

data class PostItem(
    val id: Int,
    val avatarResId: Int = R.drawable.post_community_image,
    val authorName: String = "Maks Korzh",
    val time: String = "12:00",
    val contentTextResId: Int = R.string.post_text,
    val contentImagesResId: Int = R.drawable.post_content_image,
    val metrics: List<SocialMetric> = listOf(
        SocialMetric(MetricsType.LIKES, 2_100),
        SocialMetric(MetricsType.VIEWS, 15_000),
        SocialMetric(MetricsType.COMMENTS, 7_000),
        SocialMetric(MetricsType.SHARES, 5_000),
    )
)