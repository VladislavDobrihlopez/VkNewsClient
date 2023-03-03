package com.voitov.vknewsclient.domain

import com.voitov.vknewsclient.R

data class PostItem(
    val avatarResId: Int = R.drawable.post_community_image,
    val authorName: String = "Maks Korzh",
    val time: String = "12:00",
    val contentTextResId: Int = R.string.post_text,
    val contentImagesResId: Int = R.drawable.post_content_image,
    val metrics: List<SocialMetric> = listOf(
        SocialMetric(MetricsType.LIKES, 21_000),
        SocialMetric(MetricsType.VIEWS, 100_000),
        SocialMetric(MetricsType.COMMENTS, 700),
        SocialMetric(MetricsType.SHARES, 5000),
    )
)