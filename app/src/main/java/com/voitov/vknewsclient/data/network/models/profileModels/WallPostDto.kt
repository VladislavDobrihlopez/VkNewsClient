package com.voitov.vknewsclient.data.network.models.profileModels

import com.google.gson.annotations.SerializedName

data class WallPostDto(
    @SerializedName("id") val id: Long,
    @SerializedName("owner_id") val ownerId: Long,
    @SerializedName("date") val secondsSince1970: Long,
    @SerializedName("text") val text: String,
    @SerializedName("likes") val likes: LikesInfoHolderDto,
    @SerializedName("comments") val comments: CommentsCountHolderDto,
    @SerializedName("reposts") val reposts: RepostsCountHolderDto,
    @SerializedName("views") val views: ViewsCountHolderDto,
    @SerializedName("attachments") val attachments: List<AttachedPhotoDto>?
) {
    override fun toString(): String {
        return "WallPostDto(id='$id', communityId=$ownerId, date=$secondsSince1970, likes=$likes, comments=$comments, reposts=$reposts, views=$views, attachments=$attachments)"
    }
}