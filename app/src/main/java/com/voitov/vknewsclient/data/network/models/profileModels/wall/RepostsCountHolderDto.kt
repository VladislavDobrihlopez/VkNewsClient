package com.voitov.vknewsclient.data.network.models.profileModels.wall

import com.google.gson.annotations.SerializedName

data class RepostsCountHolderDto(
    @SerializedName("count") val count: Int,
    @SerializedName("user_reposted") val userShares: Int
)