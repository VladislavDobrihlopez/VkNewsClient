package com.voitov.vknewsclient.presentation.homeScreen

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.presentation.LoadingGoingOn
import com.voitov.vknewsclient.presentation.ViewModelsFactory
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreen
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedScreenState
import com.voitov.vknewsclient.presentation.newsFeedScreen.NewsFeedViewModel
import com.voitov.vknewsclient.presentation.newsFeedScreen.NoInternetLabel

@Composable
fun HomeScreen(
    viewModelsFactory: ViewModelsFactory,
    paddingVales: PaddingValues,
    onCommentsClickListener: (PostItem) -> Unit
) {
    val viewModel: NewsFeedViewModel = viewModel(factory = viewModelsFactory)
    val postsState =
        viewModel.screenState.collectAsState(initial = NewsFeedScreenState.InitialState)
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

        is NewsFeedScreenState.ErrorState -> {
            NoInternetLabel()
            Log.d("ERROR_TEST", "some error occurred")
        }
    }
}
