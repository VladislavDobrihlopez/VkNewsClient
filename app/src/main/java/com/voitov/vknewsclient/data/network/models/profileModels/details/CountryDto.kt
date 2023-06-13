package com.voitov.vknewsclient.data.network.models.profileModels.details

import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("title") val title: String?,
    @SerializedName("id") val id: Int?
)