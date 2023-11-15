package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class AttachedPhotoDto(
    @SerializedName("photo") val photo: PhotoDto?
)