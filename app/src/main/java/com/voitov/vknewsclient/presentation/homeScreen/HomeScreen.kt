package com.voitov.vknewsclient.presentation.homeScreen

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.getApplicationComponent
import com.voitov.vknewsclient.presentation.LoadingGoingOn
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreen
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreenState
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedViewModel
import com.voitov.vknewsclient.presentation.newsFeedScreen.NoInternetLabel

@Composable
fun HomeScreen(
    paddingVales: PaddingValues,
    onCommentsClickListener: (PostItem) -> Unit
) {
    val viewModel: NewsFeedViewModel =
        viewModel(factory = getApplicationComponent().getViewModelsFactory())
    val postsState =
        viewModel.screenState.collectAsState(initial = NewsFeedScreenState.InitialState)
    ScreenContent(
        newsFeedScreenState = postsState,
        paddingVales = paddingVales,
        viewModel = viewModel
    ) { post ->
        onCommentsClickListener(post)
    }
}

@Composable
private fun ScreenContent(
    newsFeedScreenState: State<NewsFeedScreenState>,
    paddingVales: PaddingValues,
    viewModel: NewsFeedViewModel,
    clickListener: (PostItem) -> Unit
) {
    when (val currentState = newsFeedScreenState.value) {
        is NewsFeedScreenState.ShowingPostsState -> {
            NewsFeedScreen(
                paddingVales,
                currentState.posts,
                viewModel,
                currentState.isDataBeingLoaded
            ) { post ->
                clickListener(post)
            }
        }

        is NewsFeedScreenState.LoadingState -> {
            LoadingGoingOn(modifier = Modifier.fillMaxSize())
        }

        is NewsFeedScreenState.InitialState -> {
            //do nothing
        }

        is NewsFeedScreenState.ErrorState -> {
            NoInternetLabel()
            Log.d("ERROR_TEST", "some error occurred")
        }
    }
}
