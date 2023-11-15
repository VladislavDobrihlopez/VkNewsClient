package com.voitov.vknewsclient.data.network.models.profileModels.details

import com.google.gson.annotations.SerializedName

data class CountersDto(
    @SerializedName("gifts") val gifts: Int,
    @SerializedName("friends") val friends: Int,
    @SerializedName("followers") val followers: Int,
)