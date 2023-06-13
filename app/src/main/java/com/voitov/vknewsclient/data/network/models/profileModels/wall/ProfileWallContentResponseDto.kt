package com.voitov.vknewsclient.data.network.models.profileModels.wall

import com.google.gson.annotations.SerializedName

data class ProfileWallContentResponseDto(
    @SerializedName("response") val content: ProfileWallContentDto?
)