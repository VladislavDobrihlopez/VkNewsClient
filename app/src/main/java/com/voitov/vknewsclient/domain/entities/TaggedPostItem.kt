package com.voitov.vknewsclient.domain.entities

//@Immutable
data class TaggedPostItem(
    val tag: ItemTag,
    val postItem: PostItem
)