package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class CommentsContentResponseDto(
    @SerializedName("response") val content: CommentsContentDto,
)