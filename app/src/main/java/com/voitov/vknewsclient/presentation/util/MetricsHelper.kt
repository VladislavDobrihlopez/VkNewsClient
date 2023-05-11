package com.voitov.vknewsclient.presentation.util

fun shortenLengthOfMetricsIfPossible(count: Int): String {
    return if (count >= 1_000_000) {
        String.format("%.1f", (count / 1000f)) + "M"
    } else if (count >= 100_000) {
        "${count.toDouble() / 1000}K"
    } else if (count >= 1000) {
        String.format("%.1f", (count / 1000f)) + "K"
    } else {
        count.toString()
    }
}