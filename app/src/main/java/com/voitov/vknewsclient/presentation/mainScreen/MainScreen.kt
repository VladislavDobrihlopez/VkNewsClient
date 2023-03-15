package com.voitov.vknewsclient.presentation.mainScreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.voitov.vknewsclient.navigation.AppNavGraph
import com.voitov.vknewsclient.navigation.rememberNavigationState
import com.voitov.vknewsclient.presentation.commentsScreen.CommentsScreen
import com.voitov.vknewsclient.presentation.homeScreen.HomeScreen
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

const val TAG = "COMPOSE_TEST"

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

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

        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedContent = {
                HomeScreen(paddingVales = it,
                    onCommentsClickListener = { clickedPostId ->
                        navigationState.navigateToComments(clickedPostId)
                    }
                )
            },
            favoritesScreenContent = { TestScreen(screenName = "favorite screen") },
            profileScreenContent = { TestScreen(screenName = "profile screen") },
            commentsContent = { clickedPostId ->
                CommentsScreen(postId = clickedPostId) {
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