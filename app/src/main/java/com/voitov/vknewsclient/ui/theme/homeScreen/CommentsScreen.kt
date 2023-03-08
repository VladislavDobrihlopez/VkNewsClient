package com.voitov.vknewsclient.ui.theme.homeScreen

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
import com.voitov.vknewsclient.domain.PostCommentItem
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun CommentsScreen(
    postId: Int,
    comments: List<PostCommentItem>,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = { CommentsScreenTopAppBar(postId, onBackPressed) }
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
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "Comments for news with id: $postId") },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewCommentsScreenDarkTheme() {
    VkNewsClientTheme(darkTheme = true) {
        CommentsScreen(1, listOf(PostCommentItem(1, 1, 1, text = "some comment"))) {

        }
    }
}

@Preview
@Composable
fun PreviewCommentsScreenLightTheme() {
    VkNewsClientTheme(darkTheme = false) {
        CommentsScreen(1, listOf(PostCommentItem(1, 1, 1, text = "some comment"))) {

        }
    }
}