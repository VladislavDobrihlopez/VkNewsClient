package com.voitov.vknewsclient.data.network.models.profileModels.wall

import com.google.gson.annotations.SerializedName
import com.voitov.vknewsclient.data.network.models.postsFeedModels.GroupInfoDto
import com.voitov.vknewsclient.data.network.models.postsFeedModels.ProfileDto

data class ProfileWallContentDto(
    @SerializedName("items") val posts: List<WallPostDto>,
    @SerializedName("groups") val communities: List<GroupInfoDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>
)