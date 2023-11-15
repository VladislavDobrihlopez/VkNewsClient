package com.voitov.vknewsclient.domain.entities

import androidx.compose.runtime.Immutable

@Immutable
data class InnerWallPost(
    val id: Long,
    val producerId: Long,
    val producerPhotoUrl: String,
    val producerName: String,
    val postType: String,
    val date: String,
    val contentText: String,
    val contentImageUrl: List<String>?,
)