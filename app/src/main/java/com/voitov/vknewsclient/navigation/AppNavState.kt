package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

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

    fun navigateToComments(postId: Int) {
        navHostController.navigate(AppScreen.Comments.passArgs(postId))
    }
}

@Composable
fun rememberNavigationState(navHostController: NavHostController = rememberNavController()): AppNavState {
    return remember {
        AppNavState(navHostController)
    }
}