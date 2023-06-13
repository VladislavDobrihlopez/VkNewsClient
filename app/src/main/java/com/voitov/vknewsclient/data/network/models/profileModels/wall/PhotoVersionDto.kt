package com.voitov.vknewsclient.data.network.models.profileModels.wall

import com.google.gson.annotations.SerializedName

data class PhotoVersionDto(
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("url") val url: String
)
