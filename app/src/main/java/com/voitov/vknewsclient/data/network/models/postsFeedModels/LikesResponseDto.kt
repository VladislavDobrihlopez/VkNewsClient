package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class LikesResponseDto(
    @SerializedName("response") val likes: LikesCountDto
)