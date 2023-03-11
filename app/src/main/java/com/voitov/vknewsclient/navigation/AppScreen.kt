package com.voitov.vknewsclient.navigation

sealed class AppScreen(
    val route: String
) {
    object Home : AppScreen(ROUTE_HOME_SCREEN)
    object NewsFeed : AppScreen(ROUTE_NEWS_FEED_SCREEN)
    object Comments : AppScreen(ROUTE_COMMENTS_SCREEN)
    object Profile : AppScreen(ROUTE_PROFILE_SCREEN)
    object Favorites : AppScreen(ROUTE_FAVORITES_SCREEN)

    companion object {
        private const val ROUTE_HOME_SCREEN = "home"
        private const val ROUTE_NEWS_FEED_SCREEN = "news_feed"
        private const val ROUTE_COMMENTS_SCREEN = "comments"
        private const val ROUTE_PROFILE_SCREEN = "profile"
        private const val ROUTE_FAVORITES_SCREEN = "favorite"
    }
}