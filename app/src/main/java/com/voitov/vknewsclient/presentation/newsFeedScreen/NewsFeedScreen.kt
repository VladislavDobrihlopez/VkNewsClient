package com.voitov.vknewsclient.presentation.newsFeedScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.presentation.homeScreen.LoadingGoingOn
import com.voitov.vknewsclient.ui.theme.TransparentGreen
import com.voitov.vknewsclient.ui.theme.TransparentRed

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    posts: List<PostItem>,
    viewModel: NewsFeedViewModel,
    isDataBeingLoaded: Boolean,
    onCommentsClickListener: (Int) -> Unit
) {
    val scrollState = rememberLazyListState()
    val alertDialogState = remember {
        mutableStateOf(DialogState.HiddenState as DialogState)
    }

    ConfirmationDialog(alertDialogState, viewModel)

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
                viewModel.ignoreItem(post)
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
                        onCommentsClickListener(post.id.toInt())
                    },
                    onLikesClickListener = {
                        alertDialogState.value = DialogState.ShownState(post)
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

sealed class DialogState {
    object HiddenState : DialogState()
    data class ShownState(val post: PostItem) : DialogState()
}

@Composable
fun ConfirmationDialog(dialogState: MutableState<DialogState>, viewModel: NewsFeedViewModel) {
    when (val state = dialogState.value) {
        is DialogState.HiddenState -> {
            return
        }
        is DialogState.ShownState -> {
            AlertDialog(
                onDismissRequest = {
                    dialogState.value = DialogState.HiddenState
                },
                confirmButton = {
                    TextButton(onClick = {
                        dialogState.value = DialogState.HiddenState
                        viewModel.changeLikeStatus(state.post)
                    }) {
                        Text("Confirm", color = MaterialTheme.colors.onPrimary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        dialogState.value = DialogState.HiddenState
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
    }
}

