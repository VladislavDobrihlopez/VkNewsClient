package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.voitov.vknewsclient.domain.entities.PostItem

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (PostItem) -> Unit
) {
    navigation(
        startDestination = AppNavScreen.NewsFeed.route,
        route = AppNavScreen.Home.route
    ) {
        composable(AppNavScreen.NewsFeed.route) {
            newsFeedScreenContent()
        }
        composable(AppNavScreen.Comments.route, arguments = listOf(
            navArgument(AppNavScreen.Comments.ARGUMENT_KEY_POST_ITEM) {
                type = NavType.StringType
            }
        )) {
            val post = it.arguments?.getString(AppNavScreen.Comments.ARGUMENT_KEY_POST_ITEM)
                ?: throw NullPointerException("ID wasn't provided for the comments screen")
            val parsedPost = Gson().fromJson(post.decode(), PostItem::class.java)
            commentsScreenContent(parsedPost)
        }
    }
}