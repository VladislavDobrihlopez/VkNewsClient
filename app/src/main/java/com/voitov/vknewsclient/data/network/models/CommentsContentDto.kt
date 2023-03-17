package com.voitov.vknewsclient.data.network.models

import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("items") val items: List<CommentDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
)
