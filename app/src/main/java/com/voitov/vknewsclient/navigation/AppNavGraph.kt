package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    homeScreenContent: @Composable () -> Unit,
    favoritesScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
) {
    NavHost(
        startDestination = AppScreen.NewsFeed.route,
        navController = navHostController,
    ) {
        composable(AppScreen.NewsFeed.route) {
            homeScreenContent()
        }
        composable(AppScreen.Favorites.route) {
            favoritesScreenContent()
        }
        composable(AppScreen.Profile.route) {
            profileScreenContent()
        }
    }
}