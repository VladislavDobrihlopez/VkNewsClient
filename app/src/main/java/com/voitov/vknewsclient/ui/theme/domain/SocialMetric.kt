package com.voitov.vknewsclient.ui.theme.domain

data class SocialMetric(
    val type: MetricsType,
    val count: Int = COUNT_BY_DEFAULT
) {
    companion object {
        private const val COUNT_BY_DEFAULT = 0
    }
}

enum class MetricsType {
    VIEWS, SHARES, COMMENTS, LIKES
}