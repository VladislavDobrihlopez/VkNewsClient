package com.voitov.vknewsclient.data.network.models.profileModels

import com.google.gson.annotations.SerializedName

data class ViewsCountHolderDto(
    @SerializedName("count") val count: Int
)