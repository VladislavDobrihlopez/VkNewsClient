package com.voitov.vknewsclient.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.voitov.vknewsclient.MainViewModel
import com.voitov.vknewsclient.domain.PostCommentItem
import com.voitov.vknewsclient.domain.PostItem
import com.voitov.vknewsclient.ui.theme.Comment
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun CommentsScreen(
    viewModel: ViewModel,
    postItem: PostItem,
    comments: List<PostCommentItem> = listOf(
        PostCommentItem(
            id = 1,
            authorId = 101,
            text = "somethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomething"
        ),
        PostCommentItem(id = 2, authorId = 102, text = "something"),
        PostCommentItem(id = 3, authorId = 103, text = "something"),
        PostCommentItem(id = 3, authorId = 103, text = "something"),
        PostCommentItem(id = 3, authorId = 103, text = "something"),
        PostCommentItem(id = 3, authorId = 103, text = "something"),
        PostCommentItem(id = 3, authorId = 103, text = "something")
    )
) {
    Scaffold(
        topBar = { CommentsScreenTopAppBar(postItem.id) }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(comments) { comment ->
                Comment(item = comment)
            }
        }
    }
}

@Composable
fun CommentsScreenTopAppBar(
    postId: Int,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "Comments for news with id: $postId") },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        })
}

@Preview
@Composable
fun PreviewCommentsScreenDarkTheme() {
    VkNewsClientTheme(darkTheme = true) {
        CommentsScreen(MainViewModel(), PostItem(1))
    }
}

@Preview
@Composable
fun PreviewCommentsScreenLightTheme() {
    VkNewsClientTheme(darkTheme = false) {
        CommentsScreen(MainViewModel(), PostItem(1))
    }
}