package com.voitov.vknewsclient.presentation.mainScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.navigation.AppNavScreen

sealed class NavigationItem(
    val labelResId: Int,
    val icon: ImageVector,
    val screen: AppNavScreen
) {
    object Home : NavigationItem(
        labelResId = R.string.bottom_navigation_item_home,
        icon = Icons.Outlined.Home,
        screen = AppNavScreen.Home
    )

    object Favorite : NavigationItem(
        labelResId = R.string.bottom_navigation_item_favorite,
        icon = Icons.Outlined.Favorite,
        screen = AppNavScreen.Favorites
    )

    object Profile : NavigationItem(
        labelResId = R.string.bottom_navigation_item_profile,
        icon = Icons.Outlined.Person,
        screen = AppNavScreen.Profile
    )
}