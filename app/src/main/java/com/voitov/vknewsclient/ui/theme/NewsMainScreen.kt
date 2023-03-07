package com.voitov.vknewsclient.ui.theme

import android.util.Log
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.voitov.vknewsclient.ui.theme.screens.HomeScreen

const val TAG = "COMPOSE_TEST"

@Composable
fun VkNews(viewModel: MainViewModel) {
    Log.d(TAG, "vkNews")

    val selectedNavigationItem =
        viewModel.selectedNavigationItem.observeAsState(NavigationItem.Home)

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
                            viewModel.selectNavigationItem(navigationItem)
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
                        selected = navigationItem == selectedNavigationItem.value,
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onSecondary,
                    )
                }
            }
        },

        ) {
        when (selectedNavigationItem.value) {
            NavigationItem.Home -> HomeScreen(paddingVales = it, viewModel = viewModel)
            NavigationItem.Favorite -> TestScreen(screenName = "Favorite")
            NavigationItem.Profile -> TestScreen(screenName = "Profile")
        }
    }
}

@Composable
fun TestScreen(screenName: String) {
    Text(text = screenName, color = Color.DarkGray)
}

@Preview
@Composable
private fun PreviewVkNews() {
    VkNewsClientTheme {
        VkNews(MainViewModel())
    }
}