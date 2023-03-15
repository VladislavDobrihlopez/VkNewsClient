package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class RepostsDto(
    @SerializedName("count") val count: Int
)