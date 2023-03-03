package com.voitov.vknewsclient.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.domain.PostItem

const val TAG = "COMPOSE_TEST"

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
        val postState = viewModel.newsPost.observeAsState(PostItem())
        Column() {
            NewsPost(
                Modifier.padding(all = 8.dp),
                postState.value,
                onViewsClickListener = {
                    viewModel.updateMetric(it)
                },
                onSharesClickListener = {
                    viewModel.updateMetric(it)
                },
                onCommentsClickListener = {
                    viewModel.updateMetric(it)
                },
                onLikesClickListener = {
                    viewModel.updateMetric(it)
                },
            )
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