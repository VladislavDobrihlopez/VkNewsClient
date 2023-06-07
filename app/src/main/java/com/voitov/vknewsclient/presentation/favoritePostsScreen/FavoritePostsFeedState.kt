package com.voitov.vknewsclient.presentation.favoritePostsScreen

import com.voitov.vknewsclient.domain.entities.TaggedPostItem

sealed class FavoritePostsFeedState {
    object Loading : FavoritePostsFeedState()
    data class Success(val posts: List<TaggedPostItem>, val dialogData: DialogData = DialogData()) :
        FavoritePostsFeedState()

    data class DialogData(val showDialog: Boolean = false, val chosenPost: TaggedPostItem? = null)
}