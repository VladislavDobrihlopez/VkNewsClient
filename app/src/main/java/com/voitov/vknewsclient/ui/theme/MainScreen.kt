package com.voitov.vknewsclient.ui.theme

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.voitov.vknewsclient.navigation.AppNavGraph
import com.voitov.vknewsclient.navigation.rememberNavigationState
import com.voitov.vknewsclient.ui.theme.commentsScreen.CommentsScreen
import com.voitov.vknewsclient.ui.theme.homeScreen.HomeScreen

const val TAG = "COMPOSE_TEST"

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    val testLogicCommentsToPost: MutableState<Int?> = remember {
        mutableStateOf(null)
    }

    Log.d(TAG, "MainScreen")

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry =
                    navigationState.navHostController.currentBackStackEntryAsState()

                Log.d(TAG, "recomposed")
                val items = listOf(
                    NavigationItem.Profile,
                    NavigationItem.Home,
                    NavigationItem.Favorite
                )

                items.forEach { navigationItem ->
                    val bottomItemIsSelected =
                        navBackStackEntry.value?.destination?.hierarchy?.any {
                            it.route == navigationItem.screen.route
                        } ?: false

                    BottomNavigationItem(
                        onClick = {
                            if (!bottomItemIsSelected) {
                                navigationState.navigateTo(navigationItem.screen.route)
                            }
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
                        selected = bottomItemIsSelected,
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onSecondary,
                    )
                }
            }
        },

        ) {
        Log.d(TAG, "VkNews")
        val postId = testLogicCommentsToPost.value

        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedContent = {
                HomeScreen(paddingVales = it,
                    onCommentsClickListener = { clickedPostId ->
                        navigationState.navigateToComments()
                        testLogicCommentsToPost.value = clickedPostId
                    }
                )
            },
            favoritesScreenContent = { TestScreen(screenName = "favorite screen") },
            profileScreenContent = { TestScreen(screenName = "profile screen") },
            commentsContent = {
                CommentsScreen(postId = postId!!) {
                    navigationState.navHostController.popBackStack()
                }
                BackHandler {
                    navigationState.navHostController.popBackStack()
                }
            }
        )
    }
}

@Composable
fun TestScreen(screenName: String) {
    val touches = rememberSaveable {
        mutableStateOf(0)
    }
    Text(
        text = "$screenName ${touches.value}",
        color = Color.DarkGray,
        modifier = Modifier.clickable {
            touches.value = touches.value + 1
        })
}

@Preview
@Composable
private fun PreviewVkNews() {
    VkNewsClientTheme {
        MainScreen()
    }
}