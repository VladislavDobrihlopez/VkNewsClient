package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class NewsFeedContentResponseDto(
    @SerializedName("response") val content: NewsFeedContentDto
)