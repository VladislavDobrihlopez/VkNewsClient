package com.voitov.vknewsclient.presentation.favoritePostsScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.reusableUIs.IconedChip
import com.voitov.vknewsclient.presentation.reusableUIs.LoadingGoingOn
import com.voitov.vknewsclient.ui.theme.Shapes
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun FavoritePostsScreen() {
    val viewModel: FavoritesViewModel =
        viewModel(factory = getApplicationComponent().getViewModelsFactory())

    val tagsState = viewModel.tagsFlow.collectAsState(initial = TagsTabState.Loading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val currentItems = rememberSaveable {
            mutableStateOf<List<ItemTag>>(listOf())
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary,
            shape = Shapes.medium
        ) {
            when (val state = tagsState.value) {
                is TagsTabState.Loading -> {
                    LoadingGoingOn()
                }

                is TagsTabState.Success -> {
                    TagsWithMultipleChecks(state = state.tags, modifier = Modifier.padding(4.dp)) {
                        val newItems = mutableListOf<ItemTag>()
                        newItems.addAll(it)
                        currentItems.value = newItems

                        viewModel.retrievePosts(newItems)
                    }
                }
            }
        }

        ShowToast(itemTags = currentItems.value)

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        when (val feedPostsState =
            viewModel.postsFlow.collectAsState(initial = FavoritePostsFeedState.Loading).value) {
            is FavoritePostsFeedState.Loading -> {
                LoadingGoingOn()
            }

            is FavoritePostsFeedState.Success -> {
                FeedPosts(posts = feedPostsState.posts)
            }

            else -> {}
        }

    }
}

@Composable
private fun FeedPosts(posts: List<TaggedPostItem>) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 54.dp
        ),
        state = scrollState
    ) {
        items(posts) { post ->
            SavedPostCard(taggedPost = post)
        }
    }
}

@Composable
private fun ShowToast(itemTags: List<ItemTag>) {
    Toast.makeText(LocalContext.current, itemTags.toString(), Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsWithMultipleChecks(
    state: List<ItemTag>,
    modifier: Modifier = Modifier,
    onTagClickedListener: (List<ItemTag>) -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        maxItemsInEachRow = 7
    ) {
        val currentlySelectedTags = rememberSaveable {
            mutableListOf(ItemTag("default"))
        }

        state.forEach { itemTag ->
            val selected = rememberSaveable { mutableStateOf(false) }
            Box(modifier = Modifier.padding(vertical = 4.dp)) {
                IconedChip(
                    enabled = true,
                    isSelected = selected.value,
                    onClick = {
                        selected.value = !selected.value

                        if (currentlySelectedTags.contains(ItemTag("default"))) {
                            currentlySelectedTags.remove(ItemTag("default"))
                        }

                        if (currentlySelectedTags.contains(itemTag)) {
                            currentlySelectedTags.remove(itemTag)
                        } else {
                            currentlySelectedTags.add(itemTag)
                        }
                        onTagClickedListener.invoke(currentlySelectedTags)
                    },
                    painter = if (isSystemInDarkTheme())
                        painterResource(id = R.drawable.ic_check_white)
                    else
                        painterResource(id = R.drawable.ic_check_black),
                    text = itemTag.name
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        }
    }
}

@Preview
@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun PreviewFavoritePostsScreen() {
    VkNewsClientTheme(darkTheme = true) {
        FavoritePostsScreen()
    }
}