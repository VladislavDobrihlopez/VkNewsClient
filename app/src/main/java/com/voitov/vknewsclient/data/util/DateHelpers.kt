package com.voitov.vknewsclient.data.util

import java.text.SimpleDateFormat
import java.util.*

fun mapTimestampToDatePattern(timestampInSeconds: Long): String {
    val date = Date(timestampInSeconds * MILLIS_IN_SECOND)
    return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
}

private const val MILLIS_IN_SECOND = 1000