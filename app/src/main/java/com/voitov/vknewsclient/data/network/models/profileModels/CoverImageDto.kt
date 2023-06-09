package com.voitov.vknewsclient.data.network.models.profileModels

import com.google.gson.annotations.SerializedName

data class CoverImageDto(
    @SerializedName("url") val url: String
)