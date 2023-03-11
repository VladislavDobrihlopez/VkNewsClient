package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedContent: @Composable () -> Unit,
    commentsContent: @Composable () -> Unit,
    favoritesScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
) {
    NavHost(
        startDestination = AppScreen.Home.route,
        navController = navHostController,
    ) {
        homeScreenNavGraph(
            newsFeedContent = { newsFeedContent() },
            commentsContent = { commentsContent() }
        )
        composable(AppScreen.Favorites.route) {
            favoritesScreenContent()
        }
        composable(AppScreen.Profile.route) {
            profileScreenContent()
        }
    }
}