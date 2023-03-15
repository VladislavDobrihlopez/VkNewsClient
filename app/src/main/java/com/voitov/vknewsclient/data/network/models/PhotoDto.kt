package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("sizes") val photos: List<PhotoVersionDto>
)
