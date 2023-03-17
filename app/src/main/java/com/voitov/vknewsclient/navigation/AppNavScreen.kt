package com.voitov.vknewsclient.navigation

import android.net.Uri
import com.google.gson.Gson
import com.voitov.vknewsclient.domain.entities.PostItem

sealed class AppNavScreen(
    val route: String
) {
    object Home : AppNavScreen(ROUTE_HOME_SCREEN)
    object NewsFeed : AppNavScreen(ROUTE_NEWS_FEED_SCREEN)
    object Comments : AppNavScreen(ROUTE_COMMENTS_SCREEN) {
        fun passArgs(post: PostItem): String {
            return ROUTE_COMMENTS_SCREEN.replace(
                oldValue = "{$ARGUMENT_KEY_POST_ITEM}",
                newValue = Gson().toJson(post).encode()
            )
        }

        const val ARGUMENT_KEY_POST_ITEM = "post_item"
    }

    object Profile : AppNavScreen(ROUTE_PROFILE_SCREEN)
    object Favorites : AppNavScreen(ROUTE_FAVORITES_SCREEN)

    companion object {
        private const val ROUTE_HOME_SCREEN = "home"
        private const val ROUTE_NEWS_FEED_SCREEN = "news_feed"
        private const val ROUTE_COMMENTS_SCREEN = "comments/{${Comments.ARGUMENT_KEY_POST_ITEM}}"
        private const val ROUTE_PROFILE_SCREEN = "profile"
        private const val ROUTE_FAVORITES_SCREEN = "favorite"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}

fun String.decode(): String {
    return Uri.decode(this)
}