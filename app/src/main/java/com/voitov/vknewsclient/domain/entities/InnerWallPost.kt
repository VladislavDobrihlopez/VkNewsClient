package com.voitov.vknewsclient.domain.entities

data class InnerWallPost(
    val id: Long,
    val producerId: Long,
    val producerPhotoUrl: String,
    val producerName: String,
    val postType: String,
    val date: String,
    val contentText: String,
    val contentImageUrl: String?,
)