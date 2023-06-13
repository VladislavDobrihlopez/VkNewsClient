package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("sizes") val photos: List<PhotoVersionDto>
)
