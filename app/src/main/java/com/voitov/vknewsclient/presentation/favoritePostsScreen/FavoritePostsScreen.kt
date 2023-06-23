package com.voitov.vknewsclient.presentation.favoritePostsScreen

import android.os.Build
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.NoBackpack
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.reusableUIs.IconedChip
import com.voitov.vknewsclient.presentation.reusableUIs.LoadingGoingOn
import com.voitov.vknewsclient.presentation.reusableUIs.PostCard
import com.voitov.vknewsclient.presentation.reusableUIs.PostFeedback
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
        CachedPosts(viewModel = viewModel)
    }
}

@Composable
private fun CachedPosts(viewModel: FavoritesViewModel) {
    val state = viewModel.postsFlow.collectAsState(initial = FavoritePostsFeedState.Loading)
    PostsContent(state = state, onDeletePost = {
        viewModel.confirmActionOnSwipeEndToStart(it)
    }, viewModel)
}

@Composable
private fun PostsContent(
    state: State<FavoritePostsFeedState>,
    onDeletePost: (TaggedPostItem) -> Unit,
    viewModel: FavoritesViewModel
) {
    when (val feedPostsState = state.value) {
        is FavoritePostsFeedState.Loading -> {
            LoadingGoingOn()
        }

        is FavoritePostsFeedState.Success -> {
            FeedPosts(posts = feedPostsState.posts) { post ->
                onDeletePost(post)
            }

            if (feedPostsState.posts.isEmpty()) {
                NoCachedPostsYet(modifier = Modifier.padding(16.dp))
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
    }
}

@Composable
private fun NoCachedPostsYet(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(
            imageVector = Icons.Default.NoBackpack,
            contentDescription = "no posts were added yet",
            modifier = Modifier.size(144.dp),
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
        Text(modifier = Modifier.padding(vertical = 8.dp), text = stringResource(R.string.no_posts))
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
                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (page == 0) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(R.string.title_tags),
                                    style = MaterialTheme.typography.h2,
                                    textAlign = TextAlign.Start
                                )
                                Tags(
                                    modifier = Modifier.padding(4.dp),
                                    state = state,
                                    viewModel = viewModel
                                )
                            } else {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(R.string.title_tags_manager),
                                    style = MaterialTheme.typography.h2,
                                    textAlign = TextAlign.Start
                                )
                                TagsStorageManager(
                                    onAddNewTagListener = {
                                        viewModel.confirmAddTagAction()
                                    },
                                    onRemoveTagListener = {
                                        viewModel.confirmRemoveTagAction()
                                    })
                            }
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
private fun Tags(
    modifier: Modifier = Modifier,
    state: TagsTabState.Success,
    viewModel: FavoritesViewModel
) {
    val currentItems = rememberSaveable {
        mutableStateOf<List<ItemTag>>(listOf())
    }

    Box(modifier = modifier.fillMaxWidth()) {
        if (state.tags.isEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.swipe_from_right_to_left_to_add_tags)
            )
        } else {
            TagsWithMultipleChecks(tags = state.tags) {
                val newItems = mutableListOf<ItemTag>()
                newItems.addAll(it)
                currentItems.value = newItems
                viewModel.retrievePosts(newItems)
            }
        }
    }

    //ShowToast(itemTags = currentItems.value)
}

@Composable
private fun TagsStorageManager(
    modifier: Modifier = Modifier,
    onAddNewTagListener: () -> Unit,
    onRemoveTagListener: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(contentColor = TransparentGreen),
            onClick = {
                onAddNewTagListener()
            }
        ) {
            Text(stringResource(R.string.add_a_new_tag))
        }
        Button(
            colors = ButtonDefaults.buttonColors(contentColor = TransparentRed),
            onClick = {
                onRemoveTagListener()
            }
        ) {
            Text(stringResource(R.string.delete_a_tag))
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
                Text(stringResource(id = R.string.confirm), color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissToAddNewTags()
            }) {
                Text(stringResource(id = R.string.dismiss), color = MaterialTheme.colors.onPrimary)
            }
        },
        title = {
            Text(
                text = stringResource(R.string.creating_a_new_tag)
            )
        },
        text = {
            Box(
                modifier = Modifier.padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = consideringTagName.value,
                    textStyle = TextStyle(fontSize = 18.sp),
                    onValueChange = { newText -> consideringTagName.value = newText }
                )
            }
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
                Text(stringResource(id = R.string.confirm), color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissToAddNewTags()
            }) {
                Text(stringResource(id = R.string.dismiss), color = MaterialTheme.colors.onPrimary)
            }
        },
        title = {
            Text(text = stringResource(R.string.removing_a_tag))
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(stringResource(R.string.select_the_tag_you_want_to_remove))
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
                        isPostShared = post.postItem.isSharedByUser,
                        isPostLiked = post.postItem.isLikedByUser,
                        metrics = post.postItem.metrics
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
                Text(stringResource(id = R.string.confirm), color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissToRemovePost()
            }) {
                Text(stringResource(id = R.string.dismiss), color = MaterialTheme.colors.onPrimary)
            }
        },
        title = {
            Text(text = stringResource(R.string.removing_cached_post) + "${taggedPost.postItem.id}")
        },
        text = {
            Text(text = stringResource(R.string.do_you_agree_to_remove_the_post_from_cache))
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
        Text(text = stringResource(R.string.delete_item), color = Color.White, fontSize = 16.sp)
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
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            alignment = Alignment.CenterHorizontally
        ),
        maxItemsInEachRow = 5
    ) {
        val currentlySelectedTags = rememberSaveable {
            mutableListOf(*tags.toTypedArray())
        }

        tags.forEach { itemTag ->
            val selected = rememberSaveable { mutableStateOf(true) }
            Box(modifier = Modifier.padding(vertical = 4.dp)) {
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