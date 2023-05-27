package com.voitov.vknewsclient.presentation.favoritePostsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.domain.usecases.storedPosts.CacheNewsUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.GetAllNewsUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.GetNewsWithSpecificTagUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.GetTagsUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.RemoveTagUseCase
import com.voitov.vknewsclient.domain.usecases.storedPosts.SaveTagUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val getNewsWithSpecificTagUseCase: GetNewsWithSpecificTagUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val saveTagUseCase: SaveTagUseCase,
    private val removeTagUseCase: RemoveTagUseCase,
    private val cacheNewsUseCase: CacheNewsUseCase,
    private val getAllNewsUseCase: GetAllNewsUseCase
) : ViewModel() {
    fun tagsFlow(): Flow<List<ItemTag>> {
        return getTagsUseCase.invoke()
    }

    fun cacheNews(news: TaggedPostItem) {
        viewModelScope.launch {
            cacheNewsUseCase.invoke(news)
        }
    }
}