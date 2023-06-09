package com.voitov.vknewsclient.data.network.models.profileModels

import com.google.gson.annotations.SerializedName

data class CoverDto(
    @SerializedName("enabled") val enabled: Int,
    @SerializedName("images") val images: List<CoverImageDto>
)