package com.voitov.vknewsclient.data.network.models.postsFeedModels

import com.google.gson.annotations.SerializedName

data class GroupInfoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("photo_100") val photoUrl: String
)
