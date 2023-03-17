package com.voitov.vknewsclient.presentation.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreen
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreenState
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedViewModel
import com.voitov.vknewsclient.ui.theme.VkBlue

@Composable
fun HomeScreen(paddingVales: PaddingValues, onCommentsClickListener: (Int) -> Unit) {
    val viewModel: NewsFeedViewModel = viewModel()
    val postsState = viewModel.screenState.observeAsState(NewsFeedScreenState.InitialState)
    when (val currentState = postsState.value) {
        is NewsFeedScreenState.ShowingPostsState -> {
            NewsFeedScreen(
                paddingVales,
                currentState.posts,
                viewModel,
                currentState.isDataBeingLoaded
            ) {
                onCommentsClickListener(it)
            }
        }
        is NewsFeedScreenState.LoadingState -> {
            LoadingGoingOn(modifier = Modifier.fillMaxSize())
        }
        is NewsFeedScreenState.InitialState -> {
            //do nothing
        }
    }
}

@Composable
fun LoadingGoingOn(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = VkBlue)
    }
}

