package com.voitov.vknewsclient.data.network.models.profileModels

import com.google.gson.annotations.SerializedName

data class ProfileResponseDto(
    @SerializedName("response") val response: List<ProfileDataDto>
)