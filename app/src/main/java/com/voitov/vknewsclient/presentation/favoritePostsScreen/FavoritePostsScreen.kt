package com.voitov.vknewsclient.presentation.favoritePostsScreen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.reusableUIs.PostCard
import com.voitov.vknewsclient.presentation.reusableUIs.PostFeedback
import com.voitov.vknewsclient.presentation.reusableUIs.IconedChip
import com.voitov.vknewsclient.presentation.reusableUIs.LoadingGoingOn
import com.voitov.vknewsclient.ui.theme.Shapes
import com.voitov.vknewsclient.ui.theme.TransparentGreen
import com.voitov.vknewsclient.ui.theme.TransparentRed
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import kotlinx.coroutines.delay

private const val TAGS_MENU_PAGES_COUNT = 2

@Composable
fun FavoritePostsScreen() {
    val viewModel: FavoritesViewModel =
        viewModel(factory = getApplicationComponent().getViewModelsFactory())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TagsMenu(viewModel = viewModel)
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        PostsContent(viewModel = viewModel)
    }
}

@Composable
private fun PostsContent(viewModel: FavoritesViewModel) {
    when (val feedPostsState =
        viewModel.postsFlow.collectAsState(initial = FavoritePostsFeedState.Loading).value) {
        is FavoritePostsFeedState.Loading -> {
            LoadingGoingOn()
        }

        is FavoritePostsFeedState.Success -> {
            FeedPosts(posts = feedPostsState.posts) { post ->
                viewModel.confirmActionOnSwipeEndToStart(post)
            }

            if (feedPostsState.dialogData.showDialog) {
                if (feedPostsState.dialogData.chosenPost == null) {
                    throw IllegalStateException()
                }
                RemoveFromCacheConfirmationDialog(
                    taggedPost = feedPostsState.dialogData.chosenPost,
                    viewModel = viewModel
                )
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TagsMenu(viewModel: FavoritesViewModel) {
    val pagerState = rememberPagerState()
    val tagsState = viewModel.tagsFlow.collectAsState(initial = TagsTabState.Loading)

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary,
        shape = Shapes.medium
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when (val state = tagsState.value) {
                is TagsTabState.Loading -> {
                    LoadingGoingOn(modifier = Modifier.padding(8.dp))
                }

                is TagsTabState.Success -> {
                    HorizontalPager(
                        pageCount = TAGS_MENU_PAGES_COUNT,
                        beyondBoundsPageCount = TAGS_MENU_PAGES_COUNT - 1,
                        state = pagerState
                    ) { page ->
                        if (page == 0) {
                            Tags(state = state, viewModel = viewModel)
                        } else {
                            TagsStorageManager(
                                onAddNewTagListener = {
                                    viewModel.confirmAddTagAction()
                                },
                                onRemoveTagListener = {
                                    viewModel.confirmRemoveTagAction()
                                })
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(TAGS_MENU_PAGES_COUNT) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration)
                                    if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                                else
                                    if (!isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(10.dp)
                            )
                        }
                    }
                }

                is TagsTabState.OnAddNewTagConfirmation -> {
                    NewTagAdditionDialog(viewModel)
                }

                is TagsTabState.OnRemoveTagConfirmation -> {
                    RemoveTagDialog(state.tags, viewModel)
                }
            }
        }
    }
}

@Composable
private fun Tags(state: TagsTabState.Success, viewModel: FavoritesViewModel) {
    val currentItems = rememberSaveable {
        mutableStateOf<List<ItemTag>>(listOf())
    }

    TagsWithMultipleChecks(tags = state.tags, modifier = Modifier.padding(4.dp)) {
        val newItems = mutableListOf<ItemTag>()
        newItems.addAll(it)
        currentItems.value = newItems
        viewModel.retrievePosts(newItems)
    }

    ShowToast(itemTags = currentItems.value)
}

@Composable
private fun TagsStorageManager(
    onAddNewTagListener: () -> Unit,
    onRemoveTagListener: () -> Unit
) {
    Log.d("TEST_COMPOSITION", "Tags storage manager")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(contentColor = TransparentGreen),
            onClick = {
                onAddNewTagListener()
            }
        ) {
            Text("Add a new tag")
        }
        Button(
            colors = ButtonDefaults.buttonColors(contentColor = TransparentRed),
            onClick = {
                onRemoveTagListener()
            }
        ) {
            Text("Delete a tag")
        }
    }
}

@Composable
private fun NewTagAdditionDialog(viewModel: FavoritesViewModel) {
    val consideringTagName = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {
            viewModel.dismissToAddNewTags()
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.saveTag(consideringTagName.value)
            }) {
                Text("Confirm", color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissToAddNewTags()
            }) {
                Text("Dismiss", color = MaterialTheme.colors.onPrimary)
            }
        },
        title = {
            Text(text = "Adding a new tag")
        },
        text = {
            TextField(
                modifier = Modifier.padding(8.dp),
                value = consideringTagName.value,
                textStyle = TextStyle(fontSize = 18.sp),
                onValueChange = { newText -> consideringTagName.value = newText }
            )
        }
    )
}

@Composable
private fun RemoveTagDialog(itemTags: List<ItemTag>, viewModel: FavoritesViewModel) {
    val consideringTagForRemoving = remember { mutableStateOf(ItemTag("default")) }

    AlertDialog(
        onDismissRequest = {
            viewModel.dismissToAddNewTags()
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.removeTag(consideringTagForRemoving.value)
            }) {
                Text("Confirm", color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissToAddNewTags()
            }) {
                Text("Dismiss", color = MaterialTheme.colors.onPrimary)
            }
        },
        title = {
            Text(text = "Removing a tag")
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Select the tag you want to remove")
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                TagsList(itemTags) {
                    consideringTagForRemoving.value = it
                }
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsList(
    itemTags: List<ItemTag>,
    modifier: Modifier = Modifier,
    onTagClickedListener: (ItemTag) -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        maxItemsInEachRow = 7
    ) {
        val currentlySelectedTag = remember {
            mutableStateOf(ItemTag("default"))
        }

        itemTags.forEach { itemTag ->
            //val selected = remember { mutableStateOf(false) }
            Box(modifier = Modifier.padding(vertical = 4.dp)) {
                IconedChip(
                    enabled = true,
                    isSelected = itemTag == currentlySelectedTag.value,
                    onClick = {
                        currentlySelectedTag.value = itemTag
                        onTagClickedListener.invoke(itemTag)
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun FeedPosts(
    posts: List<TaggedPostItem>,
    onRemoveFromCachePostSwipeEndToStart: (TaggedPostItem) -> Unit
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 56.dp
        ),
        state = scrollState
    ) {
        items(posts, key = { it.postItem.id }) { post ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                onRemoveFromCachePostSwipeEndToStart(post)
                LaunchedEffect(Unit) {
                    delay(500)
                    dismissState.reset()
                }
            }
            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                background = {
                    DismissedPostBackground()
                },
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = {
                    FractionalThreshold(0.6f)
                }
            ) {
                PostCard(modifier = Modifier, postItem = post.postItem, PostFeedbackContent = {
                    PostTagsAssociatedByUser(post)
                    PostFeedback(
                        post.postItem.isLikedByUser,
                        post.postItem.metrics
                    )
                })
            }
        }
    }
}

@Composable
private fun PostTagsAssociatedByUser(taggedPost: TaggedPostItem) {
    Row {
        IconedChip(
            borderWidth = 1.dp,
            enabled = false,
            painter = if (isSystemInDarkTheme())
                painterResource(id = R.drawable.ic_check_white)
            else
                painterResource(id = R.drawable.ic_check_black),
            text = taggedPost.tag.name,
        )
    }
}

@Composable
private fun RemoveFromCacheConfirmationDialog(
    taggedPost: TaggedPostItem,
    viewModel: FavoritesViewModel
) {
    AlertDialog(
        onDismissRequest = {
            viewModel.dismissToRemovePost()
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.removeCachedPost(taggedPost)
            }) {
                Text("Confirm", color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissToRemovePost()
            }) {
                Text("Dismiss", color = MaterialTheme.colors.onPrimary)
            }
        },
        title = {
            Text(text = "Removing cached post ${taggedPost.postItem.id}")
        },
        text = {
            Text(text = "Do you agree to remove the post from cache?")
        }
    )
}

@Preview
@Composable
private fun DismissedPostBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
            .background(TransparentRed)
            .padding(all = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(text = "Delete item", color = Color.White, fontSize = 16.sp)
    }
}

@Composable
private fun ShowToast(itemTags: List<ItemTag>) {
    Toast.makeText(LocalContext.current, itemTags.toString(), Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsWithMultipleChecks(
    tags: List<ItemTag>,
    modifier: Modifier = Modifier,
    onTagClickedListener: (List<ItemTag>) -> Unit
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        maxItemsInEachRow = 5
    ) {
        val currentlySelectedTags = rememberSaveable {
            mutableListOf(*tags.toTypedArray())
        }

        tags.forEach { itemTag ->
            val selected = rememberSaveable { mutableStateOf(true) }
            Box(modifier = Modifier.padding(vertical = 6.dp)) {
                IconedChip(
                    enabled = true,
                    isSelected = selected.value,
                    onClick = {
                        selected.value = !selected.value

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