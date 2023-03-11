package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedContent: @Composable () -> Unit,
    commentsContent: @Composable (Int) -> Unit
) {
    navigation(
        startDestination = AppScreen.NewsFeed.route,
        route = AppScreen.Home.route
    ) {
        composable(AppScreen.NewsFeed.route) {
            newsFeedContent()
        }
        composable(AppScreen.Comments.route, arguments = listOf(
            navArgument(AppScreen.Comments.ARGUMENT_KEY_COMMENTS_ID) {
                type = NavType.IntType
            }
        )) {
            val postId = it.arguments?.getInt(AppScreen.Comments.ARGUMENT_KEY_COMMENTS_ID)
                ?: throw NullPointerException()
            commentsContent(postId)
        }
    }
}