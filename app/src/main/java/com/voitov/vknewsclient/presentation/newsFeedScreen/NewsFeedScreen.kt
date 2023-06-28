package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.ItemTag
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.reusableUIs.IconedChip
import com.voitov.vknewsclient.presentation.reusableUIs.LoadingGoingOn
import com.voitov.vknewsclient.presentation.reusableUIs.PostCard
import com.voitov.vknewsclient.ui.theme.TransparentGreen
import com.voitov.vknewsclient.ui.theme.TransparentRed
import kotlinx.coroutines.delay

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    posts: List<PostItem>,
    isDataBeingLoaded: Boolean,
    onCommentsClickListener: (PostItem) -> Unit,
) {
    val viewModel: NewsFeedScreenViewModel =
        viewModel(factory = getApplicationComponent().getViewModelsFactory())
    val state =
        viewModel.screenContentStateFlow.collectAsState(initial = NewsFeedScreenContentState.Content)
    NewsFeedScreenContent(
        state = state,
        viewModel = viewModel,
        paddingValues = paddingValues,
        posts = posts,
        isDataBeingLoaded = isDataBeingLoaded,
        onCommentsClickListener = onCommentsClickListener
    )
}

@Composable
private fun NewsFeedScreenContent(
    state: State<NewsFeedScreenContentState>,
    viewModel: NewsFeedScreenViewModel,
    paddingValues: PaddingValues,
    posts: List<PostItem>,
    isDataBeingLoaded: Boolean,
    onCommentsClickListener: (PostItem) -> Unit,
) {
    when (val currentState = state.value) {
        is NewsFeedScreenContentState.OnPostLikeActionConfirmation -> {
            ConfirmationDialog(
                title = {
                    Text(text = stringResource(R.string.alert_title_changing_status_of_like))
                },
                text = {
                    Text(text = stringResource(R.string.alert_agreement_text_change_like))
                },
                confirmation = {
                    Text(stringResource(R.string.confirm), color = MaterialTheme.colors.onPrimary)
                },
                dismissing = {
                    Text(stringResource(R.string.dismiss), color = MaterialTheme.colors.onPrimary)
                },
                onDismissRequest = {
                    viewModel.dismiss()
                },
                onConfirmationButtonClicked = {
                    viewModel.changeLikeStatus(currentState.post)
                },
                onDismissButtonClicked = {
                    viewModel.dismiss()
                }
            )
        }

        is NewsFeedScreenContentState.OnEndToStartActionConfirmation -> {
            ConfirmationDialog(
                confirmation = {
                    Text(stringResource(R.string.confirm), color = MaterialTheme.colors.onPrimary)
                },
                dismissing = {
                    Text(stringResource(R.string.dismiss), color = MaterialTheme.colors.onPrimary)
                },
                title = {
                    Text(text = stringResource(R.string.alert_title_ignore_the_post))
                },
                text = {
                    Text(text = stringResource(R.string.alert_switching_post_to_ignored_state))
                },
                onDismissRequest = {
                    viewModel.dismiss()
                },
                onConfirmationButtonClicked = {
                    viewModel.ignoreItem(currentState.post)
                },
                onDismissButtonClicked = {
                    viewModel.dismiss()
                }
            )
        }

        is NewsFeedScreenContentState.OnStartToEndActionConfirmation -> {
            CachePostIncludingTagPopUp(viewModel = viewModel) { tag ->
                viewModel.cachePost(currentState.post, tag)
            }
        }

        is NewsFeedScreenContentState.OnPostShareActionConfirmation -> {
            ConfirmationDialog(
                confirmation = {
                    Text(stringResource(R.string.confirm), color = MaterialTheme.colors.onPrimary)
                },
                dismissing = {
                    Text(stringResource(R.string.dismiss), color = MaterialTheme.colors.onPrimary)
                },
                title = {
                    Text(text = stringResource(R.string.alert_title_changing_share_status))
                },
                text = {
                    Text(text = stringResource(R.string.alert_agreement_on_sharing_post))
                },
                onDismissRequest = { viewModel.dismiss() },
                onConfirmationButtonClicked = {
                    viewModel.share(currentState.post)
                },
                onDismissButtonClicked = {
                    viewModel.dismiss()
                }
            )
        }

        is NewsFeedScreenContentState.OnEndOfPosts -> {
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.you_ve_viewed_all_the_posts),
                Toast.LENGTH_LONG
            ).show()
        }

        else -> {

        }
    }

    PostsContent(
        paddingValues = paddingValues,
        posts = posts,
        viewModel = viewModel,
        isDataBeingLoaded = isDataBeingLoaded,
        onCommentsClickListener = onCommentsClickListener,
        onLikesClickListener = { post ->
            viewModel.confirmLikeAction(post)
        },
        onSharesClickListener = { post ->
            viewModel.confirmShareAction(post)
        },
        onIgnorePostSwipeEndToStart = { post ->
            viewModel.confirmActionOnSwipeEndToStart(post)
        },
        onCachePostSwipeStartToEnd = { post ->
            viewModel.confirmActionOnSwipeStartToEnd(post)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun PostsContent(
    paddingValues: PaddingValues,
    posts: List<PostItem>,
    viewModel: NewsFeedScreenViewModel,
    isDataBeingLoaded: Boolean,
    onCommentsClickListener: (PostItem) -> Unit,
    onLikesClickListener: (PostItem) -> Unit,
    onSharesClickListener: (PostItem) -> Unit,
    onIgnorePostSwipeEndToStart: (PostItem) -> Unit,
    onCachePostSwipeStartToEnd: (PostItem) -> Unit
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollState
    ) {
        items(items = posts, key = { it.id }) { post ->
            val dismiss = rememberDismissState()

            if (dismiss.isDismissed(DismissDirection.EndToStart)) {
                onIgnorePostSwipeEndToStart(post)
                LaunchedEffect(Unit) {
                    delay(500)
                    dismiss.reset()
                }
            } else if (dismiss.isDismissed(DismissDirection.StartToEnd)) {
                onCachePostSwipeStartToEnd(post)
                LaunchedEffect(Unit) {
                    delay(500)
                    dismiss.reset()
                }
            } else {
                LaunchedEffect(Unit) {
                    dismiss.reset()
                }
            }

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismiss,
                background = {
                    Row(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .align(Alignment.CenterVertically),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_bookmark),
                                contentDescription = stringResource(R.string.bookmark),
                                colorFilter = ColorFilter.tint(TransparentGreen),
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_recycling_bin),
                                contentDescription = stringResource(R.string.trash_post),
                                colorFilter = ColorFilter.tint(TransparentRed),
                            )
                        }
                    }
                },
                dismissThresholds = { dismissedDirection ->
                    if (dismissedDirection == DismissDirection.StartToEnd) {
                        FractionalThreshold(0.6f)
                    } else {
                        FractionalThreshold(0.6f)
                    }
                }
            ) {
                PostCard(
                    postItem = post,
                    onCommentsClickListener = {
                        onCommentsClickListener(post)
                    },
                    onLikesClickListener = {
                        onLikesClickListener(post)
                    },
                    onSharesClickListener = {
                        onSharesClickListener(post)
                    }
                )

            }
        }
        item {
            if (isDataBeingLoaded) {
                LoadingGoingOn(modifier = Modifier.fillMaxWidth())
            } else {
                SideEffect {
                    viewModel.loadContinuingPosts()
                }
            }
        }
    }
}

@Composable
private fun ConfirmationDialog(
    confirmation: @Composable RowScope.() -> Unit,
    dismissing: @Composable RowScope.() -> Unit,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmationButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmationButtonClicked()
            }) {
                confirmation()
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismissButtonClicked()
            }) {
                dismissing()
            }
        },
        title = {
            title()
        },
        text = {
            text()
        }
    )
}

@Composable
private fun CachePostIncludingTagPopUp(
    viewModel: NewsFeedScreenViewModel,
    onButtonClickListener: (ItemTag) -> Unit
) {
    val selectedTag = remember {
        mutableStateOf<ItemTag?>(null)
    }

    Popup(
        popupPositionProvider = WindowCenterOffsetPositionProvider(),
        properties = PopupProperties(focusable = true, dismissOnClickOutside = true),
        onDismissRequest = { viewModel.dismiss() },
    ) {

        Surface(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.surface.copy(alpha = 0.85f),
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.alert_select_tags),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                val tagsState = viewModel.tagsFlow.collectAsState(initial = TagsTabState.Loading)

                when (val state = tagsState.value) {
                    TagsTabState.Loading -> {
                        LoadingGoingOn()
                    }

                    is TagsTabState.Success -> {
                        Tags(
                            state = state.tags,
                            modifier = Modifier.weight(weight = 1f, fill = false)
                        ) {
                            selectedTag.value = it
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {
                    selectedTag.value?.let {
                        onButtonClickListener(it)
                        viewModel.dismiss()
                    }
                }) {
                    Text(text = stringResource(R.string.cache_the_post))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Tags(
    state: List<ItemTag>,
    modifier: Modifier = Modifier,
    onTagClickedListener: (ItemTag) -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        maxItemsInEachRow = 7
    ) {
        val currentlySelectedTag = remember {
            mutableStateOf(ItemTag(DEFAULT))
        }

        state.forEach { itemTag ->
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

private const val DEFAULT = "default"

private class WindowCenterOffsetPositionProvider(
    private val x: Int = 0,
    private val y: Int = 0
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(
            (windowSize.width - popupContentSize.width) / 2 + x,
            (windowSize.height - popupContentSize.height) - 10 + y
        )
    }
}
