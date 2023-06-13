package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.voitov.vknewsclient.domain.entities.PostItem

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedContent: @Composable () -> Unit,
    commentsContent: @Composable (PostItem) -> Unit,
    favoritesScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable (Long?) -> Unit,
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
        composable(
            AppNavScreen.Profile.route,
            arguments = listOf(navArgument(AppNavScreen.Profile.ARGUMENT_KEY_AUTHOR_ID) {
                type = NavType.LongType
            })
        ) {
            val authorId = it.arguments?.getLong(AppNavScreen.Profile.ARGUMENT_KEY_AUTHOR_ID)
                ?: throw NullPointerException("ID wasn't provided for the profile screen")

            if (authorId == 0L) {
                profileScreenContent(null)
            } else {
                profileScreenContent(authorId)
            }
        }
    }
}