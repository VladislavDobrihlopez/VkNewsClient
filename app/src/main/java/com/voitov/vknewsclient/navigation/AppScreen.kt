package com.voitov.vknewsclient.navigation

sealed class AppScreen(
    val route: String
) {
    object NewsFeed : AppScreen(ROUTE_HOME_SCREEN)
    object Profile : AppScreen(ROUTE_PROFILE_SCREEN)
    object Favorites : AppScreen(ROUTE_FAVORITES_SCREEN)

    companion object {
        private const val ROUTE_HOME_SCREEN = "news_feed"
        private const val ROUTE_PROFILE_SCREEN = "profile"
        private const val ROUTE_FAVORITES_SCREEN = "favorite"
    }
}