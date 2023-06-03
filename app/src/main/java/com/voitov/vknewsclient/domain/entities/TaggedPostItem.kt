package com.voitov.vknewsclient.domain.entities

import androidx.compose.runtime.Immutable

//@Immutable
@Immutable
data class TaggedPostItem(
    val tag: ItemTag,
    val postItem: PostItem
)