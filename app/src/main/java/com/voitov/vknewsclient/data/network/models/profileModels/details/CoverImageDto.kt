package com.voitov.vknewsclient.data.network.models.profileModels.details

import com.google.gson.annotations.SerializedName

data class CoverImageDto(
    @SerializedName("url") val url: String
)