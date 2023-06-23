package com.voitov.vknewsclient.navigation

import android.util.Log
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
    private var previousNavigatedProfileScreen: ProfileAuthor? = null

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
        Log.d("TEST_NAVIGATION", "navigated $author")
        Log.d("TEST_NAVIGATION", "previous $previousNavigatedProfileScreen")

        val prevNavigatedProfileScreen = previousNavigatedProfileScreen

        val stateNeedToBeRestored = !(prevNavigatedProfileScreen == null || prevNavigatedProfileScreen != author)
        previousNavigatedProfileScreen = author

        Log.d("TEST_NAVIGATION", "previous $stateNeedToBeRestored")

        navHostController.navigate(route = AppNavScreen.Profile.passArgs(author)) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true

            restoreState = stateNeedToBeRestored
        }
    }
}

@Composable
fun rememberNavigationState(navHostController: NavHostController = rememberNavController()): AppNavState {
    return remember {
        AppNavState(navHostController)
    }
}