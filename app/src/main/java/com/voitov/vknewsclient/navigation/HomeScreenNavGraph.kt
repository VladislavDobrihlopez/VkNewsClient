package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedContent: @Composable () -> Unit,
    commentsContent: @Composable () -> Unit
) {
    navigation(
        startDestination = AppScreen.NewsFeed.route,
        route = AppScreen.Home.route
    ) {
        composable(AppScreen.NewsFeed.route) {
            newsFeedContent()
        }
        composable(AppScreen.Comments.route) {
            commentsContent()
        }
    }
}