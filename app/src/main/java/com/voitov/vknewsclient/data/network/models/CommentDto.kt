package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("id") val id: Long,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("date") val date: Long,
    @SerializedName("text") val text: String,
    @SerializedName("likes") val likeInfo: CommentLikeInfoDto
)