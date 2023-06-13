package com.voitov.vknewsclient.data.network.models.profileModels.wall

import com.google.gson.annotations.SerializedName

data class WallPostDto(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("owner_id") val ownerId: Long,
    @SerializedName("date") val secondsSince1970: Long,
    @SerializedName("text") val text: String,
    @SerializedName("likes") val likes: LikesInfoHolderDto,
    @SerializedName("comments") val comments: CommentsCountHolderDto,
    @SerializedName("reposts") val reposts: RepostsCountHolderDto,
    @SerializedName("views") val views: ViewsCountHolderDto?,
    @SerializedName("attachments") val attachments: List<AttachedPhotoDto>?,
    @SerializedName("copy_history") val copyHistory: List<InnerWallPostDto>? = null
) {

    override fun toString(): String {
        return "WallPostDto(id=$id, type='$type', fromId=$fromId, ownerId=$ownerId, secondsSince1970=$secondsSince1970, text='$text', likes=$likes, comments=$comments, reposts=$reposts, views=$views, attachments=$attachments, copyHistory=$copyHistory)"
    }
}