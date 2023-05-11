package com.voitov.vknewsclient.presentation.favoritePostsScreen

import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.domain.usecases.GetPostItemTagsUseCase
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val getPostItemTagsUseCase: GetPostItemTagsUseCase
): ViewModel() {
    val tagsFlow = getPostItemTagsUseCase.invoke()
}