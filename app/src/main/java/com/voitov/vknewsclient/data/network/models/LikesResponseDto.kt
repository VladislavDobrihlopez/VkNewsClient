package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class LikesResponseDto(
    @SerializedName("response") val likes: LikesCountDto
)