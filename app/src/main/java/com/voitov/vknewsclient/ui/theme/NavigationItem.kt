package com.voitov.vknewsclient.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.navigation.AppScreen

sealed class NavigationItem(
    val labelResId: Int,
    val icon: ImageVector,
    val screen: AppScreen
) {
    object Home : NavigationItem(
        labelResId = R.string.bottom_navigation_item_home,
        icon = Icons.Outlined.Home,
        screen = AppScreen.Home
    )

    object Favorite : NavigationItem(
        labelResId = R.string.bottom_navigation_item_favorite,
        icon = Icons.Outlined.Favorite,
        screen = AppScreen.Favorites
    )

    object Profile : NavigationItem(
        labelResId = R.string.bottom_navigation_item_profile,
        icon = Icons.Outlined.Person,
        screen = AppScreen.Profile
    )
}