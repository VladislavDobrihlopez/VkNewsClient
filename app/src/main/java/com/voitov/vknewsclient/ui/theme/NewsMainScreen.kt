package com.voitov.vknewsclient.ui.theme

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R
import kotlinx.coroutines.launch

const val TAG = "COMPOSE_TEST"

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun VkNews(viewModel: MainViewModel) {
    Log.d(TAG, "vkNews")

    Scaffold(
        bottomBar = {
            BottomNavigation {
                Log.d(TAG, "recomposed")
                val items =
                    listOf(NavigationItem.Profile, NavigationItem.Home, NavigationItem.Favorite)
                val startFocusedSection = 0
                val selectedItemIndex = remember {
                    mutableStateOf(startFocusedSection)
                }

                items.forEachIndexed { index, navigationItem ->
                    BottomNavigationItem(
                        onClick = {
                            selectedItemIndex.value = index
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = stringResource(navigationItem.labelResId)
                            )
                        },
                        label = {
                            Text(stringResource(navigationItem.labelResId))
                        },
                        selected = selectedItemIndex.value == index,
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onSecondary,
                    )
                }
            }
        },

        ) {
        val postsState = viewModel.newsPost.observeAsState(listOf())
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 72.dp //bottomBar = 56dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = scrollState
        ) {
            items(items = postsState.value, key = { it.id }) { post ->
                val dismiss = rememberDismissState()

                // change in the future
                if (dismiss.isDismissed(DismissDirection.EndToStart)) {
                    viewModel.remove(post.id)
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
                                .align(CenterVertically),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_bookmark),
                                    contentDescription = stringResource(R.string.bookmark),
                                    colorFilter = ColorFilter.tint(Color.Green.copy(alpha = 0.8f)),
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_recycling_bin),
                                    contentDescription = stringResource(R.string.trash_post),
                                    colorFilter = ColorFilter.tint(Color.Red.copy(alpha = 0.8f)),
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
                    NewsPost(
                        postItem = post,
                        onViewsClickListener = {
                            viewModel.updateMetric(post.id, it)
                        },
                        onSharesClickListener = {
                            viewModel.updateMetric(post.id, it)
                        },
                        onCommentsClickListener = {
                            viewModel.updateMetric(post.id, it)
                        },
                        onLikesClickListener = {
                            viewModel.updateMetric(post.id, it)
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewVkNews() {
    VkNewsClientTheme {
        VkNews(MainViewModel())
    }
}