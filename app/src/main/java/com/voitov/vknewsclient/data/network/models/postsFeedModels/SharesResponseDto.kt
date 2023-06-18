package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class SharesResponseDto(
    @SerializedName("response") val response: SharingProcessInfoDto
)