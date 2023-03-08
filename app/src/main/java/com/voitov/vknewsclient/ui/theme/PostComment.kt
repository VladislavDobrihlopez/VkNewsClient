package com.voitov.vknewsclient.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.PostCommentItem

@Composable
fun Comment(item: PostCommentItem) {
    Row {
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(35.dp),
            painter = painterResource(id = item.avatarResId),
            contentDescription = "image"
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${item.authorName} ${item.authorLastName} id: ${item.authorId}",
                color = MaterialTheme.colors.onPrimary,
            )
            Text(
                text = item.text,
                color = MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = item.publicationTime,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSecondary
            )
        }
        Row(
            modifier = Modifier.align(Alignment.Bottom),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconWithText(pictResId = R.drawable.ic_like, text = item.likes.toString()) {

            }
        }
    }
}

@Preview
@Composable
private fun PreviewCommentDarkTheme() {
    VkNewsClientTheme(darkTheme = true) {
        Card {
            Comment(
                item = PostCommentItem(
                    id = 1,
                    authorId = 1,
                    text = "somethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomething"
                )
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCommentLightTheme() {
    VkNewsClientTheme(darkTheme = false) {
        Card {
            Comment(
                item = PostCommentItem(
                    id = 1,
                    authorId = 1,
                    text = "somethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomething"
                )
            )
        }
    }
}