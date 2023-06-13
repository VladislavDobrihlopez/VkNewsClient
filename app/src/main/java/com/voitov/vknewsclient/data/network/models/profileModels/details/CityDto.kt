package com.voitov.vknewsclient.data.network.models.profileModels.details

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("title") val name: String?,
    @SerializedName("id") val id: Int?
)