package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.entities.PostItem

class AppNavState(
    val navHostController: NavHostController
) {
    fun navigateTo(route: String) {
        navHostController.navigate(route = route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToComments(post: PostItem) {
        navHostController.navigate(AppNavScreen.Comments.passArgs(post))
    }

    fun navigateToProfile(author: ProfileAuthor) {
        navHostController.navigate(route = AppNavScreen.Profile.passArgs(author)) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true

            if (author is ProfileAuthor.Me) {
                restoreState = true
            }
        }
    }
}

@Composable
fun rememberNavigationState(navHostController: NavHostController = rememberNavController()): AppNavState {
    return remember {
        AppNavState(navHostController)
    }
}