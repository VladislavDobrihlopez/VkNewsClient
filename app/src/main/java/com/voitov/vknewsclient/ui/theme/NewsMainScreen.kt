package com.voitov.vknewsclient.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.ui.theme.domain.PostItem

const val TAG = "COMPOSE_TEST"

@Composable
fun VkNews() {
    val postState = remember {
        mutableStateOf(PostItem())
    }
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
        Column() {
            repeat(1) {
                NewsPost(
                    Modifier.padding(all = 8.dp),
                    postState.value
                ) {
                    val oldPostInfo = postState.value
                    val oldFeedbackInfo = oldPostInfo.metrics

                    val itemIndex =
                        oldFeedbackInfo.indexOf(oldFeedbackInfo.getMetricByType(it.type))
                    val oldItemMetric = oldFeedbackInfo[itemIndex]
                    val newItemMetric = oldItemMetric.copy(count = oldItemMetric.count + 1)

                    val newFeedbackInfo = oldFeedbackInfo.toMutableList()

//                    for ((index, item) in oldFeedbackInfo.withIndex()) {
//                        if (index != itemIndex && item.type == it.type) {
//                            newFeedbackInfo.add(item)
//                        }
//                    }

                    newFeedbackInfo[itemIndex] = newItemMetric
                    postState.value = oldPostInfo.copy(metrics = newFeedbackInfo)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewVkNews() {
    VkNewsClientTheme {
        VkNews()
    }
}