package com.voitov.vknewsclient.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.MetricsType

const val TAG = "COMPOSE_TEST"

@OptIn(ExperimentalMaterialApi::class)
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
            state = scrollState
        ) {
            items(postsState.value, key = { it.id }) { post ->
                val dismiss = rememberDismissState()

                // change in the future
                if (dismiss.isDismissed(DismissDirection.EndToStart)) {
                    viewModel.remove(post.id)
                } else {
                }

                SwipeToDismiss(
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
                    dismissThresholds = {
                        if (it == DismissDirection.StartToEnd) {
                            FractionalThreshold(0.8f)
                        } else {
                            FractionalThreshold(0.8f)
                        }
                    }
                ) {
                    NewsPost(
                        Modifier.padding(all = 8.dp),
                        post,
                        onViewsClickListener = {
                            viewModel.updateMetric(post.id, MetricsType.VIEWS)
                        },
                        onSharesClickListener = {
                            viewModel.updateMetric(post.id, MetricsType.SHARES)
                        },
                        onCommentsClickListener = {
                            viewModel.updateMetric(post.id, MetricsType.COMMENTS)
                        },
                        onLikesClickListener = {
                            viewModel.updateMetric(post.id, MetricsType.LIKES)
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