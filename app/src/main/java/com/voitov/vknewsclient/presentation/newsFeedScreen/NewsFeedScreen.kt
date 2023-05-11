package com.voitov.vknewsclient.presentation.newsFeedScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.LoadingGoingOn
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
        viewModel.screenContentStateFlow.collectAsState(initial = NewsFeedScreenContentState.Content).value

    when (state) {
        is NewsFeedScreenContentState.OnPostLikeActionConfirmation -> {
            LikeConfirmationDialog(post = state.post, viewModel = viewModel)
        }

        is NewsFeedScreenContentState.OnEndToStartActionConfirmation -> {
            IgnoreConfirmationDialog(post = state.post, viewModel = viewModel)
        }

        is NewsFeedScreenContentState.OnStartToEndActionConfirmation -> {

        }

        else -> {}
    }

    NewsFeedScreenContent(
        paddingValues = paddingValues,
        posts = posts,
        viewModel = viewModel,
        isDataBeingLoaded = isDataBeingLoaded,
        onCommentsClickListener = onCommentsClickListener,
        onLikesClickListener = { post ->
            viewModel.confirmLikeAction(post)
        },
        onIgnorePostSwipeEndToStart = { post ->
            viewModel.confirmActionOnSwipeEndToStart(post)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun NewsFeedScreenContent(
    paddingValues: PaddingValues,
    posts: List<PostItem>,
    viewModel: NewsFeedScreenViewModel,
    isDataBeingLoaded: Boolean,
    onCommentsClickListener: (PostItem) -> Unit,
    onLikesClickListener: (PostItem) -> Unit,
    onIgnorePostSwipeEndToStart: (PostItem) -> Unit
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

            // change in the future
            if (dismiss.isDismissed(DismissDirection.EndToStart)) {
                onIgnorePostSwipeEndToStart(post)
                LaunchedEffect(Unit) {
                    delay(500)
                    dismiss.reset()
                }
            } else if (dismiss.isDismissed(DismissDirection.StartToEnd)) {
                LaunchedEffect(Unit) {
                    delay(500)
                    dismiss.reset()
                }
            } else {
                //todo implement bookmark page
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
                            modifier = Modifier
                                .weight(1f)
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
private fun LikeConfirmationDialog(post: PostItem, viewModel: NewsFeedScreenViewModel) {
    AlertDialog(
        onDismissRequest = {
            viewModel.dismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.changeLikeStatus(post)
            }) {
                Text("Confirm", color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismiss()
            }) {
                Text("Dismiss", color = MaterialTheme.colors.onPrimary)
            }
        },
        title = {
            Text(text = "Changing status of like")
        },
        text = {
            Text(text = "Do you agree to change the status of like?")
        }
    )
}

@Composable
private fun IgnoreConfirmationDialog(post: PostItem, viewModel: NewsFeedScreenViewModel) {
    AlertDialog(
        onDismissRequest = {
            viewModel.dismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.ignoreItem(post)
            }) {
                Text("Confirm", color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismiss()
            }) {
                Text("Dismiss", color = MaterialTheme.colors.onPrimary)
            }
        },
        title = {
            Text(text = "Do you want to now see this posts in the future?")
        },
        text = {
            Text(text = "Do you agree to ignore the post?")
        }
    )
}
