package com.voitov.vknewsclient.presentation.mainScreen

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.navigation.AppNavGraph
import com.voitov.vknewsclient.navigation.AppNavScreen
import com.voitov.vknewsclient.navigation.rememberNavigationState
import com.voitov.vknewsclient.presentation.commentsScreen.CommentsScreen
import com.voitov.vknewsclient.presentation.favoritePostsScreen.FavoritePostsScreen
import com.voitov.vknewsclient.presentation.postsFeedScreen.HomeScreen
import com.voitov.vknewsclient.presentation.profileScreen.ProfileScreen
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun MainScreen(onScreenReady: () -> Unit) {
    val navigationState = rememberNavigationState()

    LaunchedEffect(key1 = Unit) {
        onScreenReady()
    }

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry =
                    navigationState.navHostController.currentBackStackEntryAsState()

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
                                if (navigationItem == NavigationItem.Profile) {
                                    navigationState.navigateToProfile(ProfileAuthor.Me)
                                } else {
                                    navigationState.navigateTo(navigationItem.screen.route)
                                }
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
        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedScreenContent = {
                HomeScreen(
                    paddingValues = it,
                    onCommentsClickListener = { clickedPost ->
                        navigationState.navigateToComments(clickedPost)
                    }
                )
            },
            favoritesScreenContent = {
                FavoritePostsScreen()
            },
            profileScreenContent = { author ->
                ProfileScreen(author)
                BackHandler {
                    navigationState.navigateTo(AppNavScreen.Home.route)
                }
            },
            commentsScreenContent = { clickedPost ->
                CommentsScreen(post = clickedPost,
                    onAuthorPhotoClickListener = {
                        navigationState.navigateToProfile(ProfileAuthor.OtherPerson(it.authorId))
                    },
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    })
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview
@Composable
private fun PreviewVkNews() {
    VkNewsClientTheme {
        MainScreen() {

        }
    }
}