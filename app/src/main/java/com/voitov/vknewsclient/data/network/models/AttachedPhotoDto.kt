package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class AttachedPhotoDto(
    @SerializedName("photo") val photo: PhotoDto?
)