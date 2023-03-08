package com.voitov.vknewsclient.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.voitov.vknewsclient.navigation.AppNavGraph
import com.voitov.vknewsclient.navigation.AppScreen
import com.voitov.vknewsclient.MainViewModel
import com.voitov.vknewsclient.ui.theme.NavigationItem
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

const val TAG = "COMPOSE_TEST"

@Composable
fun MainScreen(viewModel: MainViewModel) {
    Log.d(TAG, "vkNews")
    val navHostController = rememberNavController()
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: ""

    Scaffold(
        bottomBar = {
            BottomNavigation {
                Log.d(TAG, "recomposed")
                val items = listOf(
                    NavigationItem.Profile,
                    NavigationItem.Home,
                    NavigationItem.Favorite
                )

                items.forEach { navigationItem ->
                    BottomNavigationItem(
                        onClick = {
                            navHostController.navigate(navigationItem.screen.route) {
                                popUpTo(AppScreen.NewsFeed.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = stringResource(navigationItem.labelResId)
                            )
                        },
                        label = {
                            Text(stringResource(navigationItem.labelResId))
                        },
                        selected = navigationItem.screen.route == currentRoute,
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onSecondary,
                    )
                }
            }
        },

        ) {
        Log.d(TAG, "VkNews")
        AppNavGraph(
            navHostController = navHostController,
            homeScreenContent = { HomeScreen(paddingVales = it, viewModel = viewModel) },
            favoritesScreenContent = { TestScreen(screenName = "favorite screen") },
            profileScreenContent = { TestScreen(screenName = "profile screen") }
        )
    }
}

@Composable
fun TestScreen(screenName: String) {
    val touches = rememberSaveable {
        mutableStateOf(0)
    }
    Text(
        text = "$screenName ${touches.value}",
        color = Color.DarkGray,
        modifier = Modifier.clickable {
            touches.value = touches.value + 1
        })
}

@Preview
@Composable
private fun PreviewVkNews() {
    VkNewsClientTheme {
        MainScreen(MainViewModel())
    }
}