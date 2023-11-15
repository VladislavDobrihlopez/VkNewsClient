package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class SharingProcessInfoDto(
    @SerializedName("reposts_count") val shares: Int,
    @SerializedName("likes_count") val likes: Int,
)