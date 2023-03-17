package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.voitov.vknewsclient.domain.entities.PostItem

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedContent: @Composable () -> Unit,
    commentsContent: @Composable (PostItem) -> Unit,
    favoritesScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
) {
    NavHost(
        startDestination = AppNavScreen.Home.route,
        navController = navHostController,
    ) {
        homeScreenNavGraph(
            newsFeedContent = newsFeedContent,
            commentsContent = commentsContent
        )
        composable(AppNavScreen.Favorites.route) {
            favoritesScreenContent()
        }
        composable(AppNavScreen.Profile.route) {
            profileScreenContent()
        }
    }
}