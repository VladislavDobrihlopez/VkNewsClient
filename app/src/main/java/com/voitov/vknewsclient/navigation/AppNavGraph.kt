package com.voitov.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.voitov.vknewsclient.domain.ProfileAuthor
import com.voitov.vknewsclient.domain.entities.PostItem

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (PostItem) -> Unit,
    favoritesScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable (ProfileAuthor) -> Unit,
) {
    NavHost(
        startDestination = AppNavScreen.Home.route,
        navController = navHostController,
    ) {
        homeScreenNavGraph(
            newsFeedScreenContent = newsFeedScreenContent,
            commentsScreenContent = commentsScreenContent
        )
        composable(AppNavScreen.Favorites.route) {
            favoritesScreenContent()
        }
        composable(
            AppNavScreen.Profile.route,
            arguments = listOf(
                navArgument(AppNavScreen.Profile.ARGUMENT_KEY_AUTHOR) {
                    type = NavType.StringType
                },
                navArgument(AppNavScreen.Profile.ARGUMENT_KEY_AUTHOR_TYPE) {
                    type = NavType.StringType
                })
        ) {
            val author = it.arguments?.getString(AppNavScreen.Profile.ARGUMENT_KEY_AUTHOR)
                ?: throw NullPointerException("Author wasn't provided for the profile screen")

            val authorType = it.arguments?.getString(AppNavScreen.Profile.ARGUMENT_KEY_AUTHOR_TYPE)
                ?: throw NullPointerException("Author type wasn't provided for the profile screen")

            when (authorType) {
                ProfileAuthor.ME -> {
                    profileScreenContent(
                        Gson().fromJson(
                            author.decode(),
                            ProfileAuthor.Me::class.java
                        )
                    )
                }

                ProfileAuthor.OTHER -> {
                    profileScreenContent(
                        Gson().fromJson(
                            author.decode(),
                            ProfileAuthor.OtherPerson::class.java
                        )
                    )
                }

                else -> {
                    throw IllegalStateException("Unknown type of author")
                }
            }
        }
    }
}