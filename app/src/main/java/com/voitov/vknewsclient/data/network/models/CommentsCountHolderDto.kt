package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class CommentsCountHolderDto(
    @SerializedName("count") val count: Int
)