package com.voitov.vknewsclient.presentation.favoritePostsScreen

import com.voitov.vknewsclient.domain.entities.ItemTag

sealed class TagsTabState {
    object Loading : TagsTabState()
    data class Success(val tags: List<ItemTag>) : TagsTabState()
}