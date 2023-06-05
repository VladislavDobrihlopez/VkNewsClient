package com.voitov.vknewsclient.presentation.favoritePostsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.usecases.storedPosts.CachePostUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.GetAllPostsUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.GetPostsWithSpecificTagUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.GetTagsUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.RemoveTagUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.SaveTagUseCase
import com.voitov.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val getPostsWithSpecificTagUseCase: GetPostsWithSpecificTagUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val saveTagUseCase: SaveTagUseCase,
    private val removeTagUseCase: RemoveTagUseCase,
    private val cachePostUseCase: CachePostUseCase,
    private val getAllPostsUseCase: GetAllPostsUseCase
) : ViewModel() {
    private var cachedTags = listOf<ItemTag>()

    private val confirmationEvents = MutableSharedFlow<TagsTabState>()
    private val changeStateFlow = flow {
        confirmationEvents.collect {
            emit(it)
        }
    }

    val tagsFlow: Flow<TagsTabState> = getTagsUseCase()
        .onStart { delay(1000) }
        .map {
            Log.d("TEST_POSTS_FLOW", "tags mapped ${it.toString()}")
            TagsTabState.Success(it) as TagsTabState
        }.stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TagsTabState.Loading
        ).mergeWith(changeStateFlow)
        .onEach {
            if (it is TagsTabState.Success) {
                cachedTags = it.tags.toList()
            }
        }

    val postsFlow: Flow<FavoritePostsFeedState> = getAllPostsUseCase()
        .map {
            Log.d("TEST_POSTS_FLOW", "posts mapped ${it.toString()}")
            FavoritePostsFeedState.Success(it) as FavoritePostsFeedState
        }.stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FavoritePostsFeedState.Loading
        )

    fun cacheNews(news: TaggedPostItem) {
        viewModelScope.launch {
            cachePostUseCase.invoke(news)
        }
    }

    fun saveTag(tagName: String) {
        viewModelScope.launch {
            saveTagUseCase(ItemTag(tagName))
        }
    }

    fun removeTag(tagName: String) {
        viewModelScope.launch {
            removeTagUseCase(cachedTags.find { it.name == tagName }!!)
        }
    }

    fun removeTag(itemTag: ItemTag) {
        viewModelScope.launch {
            removeTagUseCase(itemTag)
        }
    }

    fun retrievePosts(tags: List<ItemTag>) {
        viewModelScope.launch {
            getPostsWithSpecificTagUseCase(tags)
        }
    }

    fun confirmAddTagAction() {
        viewModelScope.launch {
            confirmationEvents.emit(TagsTabState.OnAddNewTagConfirmation)
        }
    }

    fun confirmRemoveTagAction() {
        viewModelScope.launch {
            confirmationEvents.emit(TagsTabState.OnRemoveTagConfirmation(cachedTags))
        }
    }

    fun dismiss() {
        viewModelScope.launch {
            confirmationEvents.emit(TagsTabState.Success(cachedTags))
        }
    }
}