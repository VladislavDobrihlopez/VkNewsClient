package com.voitov.vknewsclient.presentation.commentsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.entities.PostCommentItem
import com.voitov.vknewsclient.presentation.reusableUIs.IconFollowedByText

@Composable
fun Comment(
    item: PostCommentItem,
    onAuthorPhotoClickListener: () -> Unit,
) {
    Row {
        AsyncImage(
            modifier = Modifier
                .clickable {
                    onAuthorPhotoClickListener()
                }
                .clip(CircleShape)
                .size(35.dp),
            model = item.avatarImageUrl,
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
                text = item.date,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSecondary
            )
        }
        Row(
            modifier = Modifier.align(Alignment.Bottom),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconFollowedByText(pictResId = R.drawable.ic_like, text = item.likesCount.toString()) {

            }
        }
    }
}

//@Preview
//@Composable
//private fun PreviewCommentDarkTheme() {
//    VkNewsClientTheme(darkTheme = true) {
//        Card {
//            Comment(
//                item = PostCommentItem(
//                    id = 1,
//                    postId = 1,
//                    authorId = 1,
//                    text = "somethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomething"
//                )
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun PreviewCommentLightTheme() {
//    VkNewsClientTheme(darkTheme = false) {
//        Card {
//            Comment(
//                item = PostCommentItem(
//                    id = 1,
//                    postId = 1,
//                    authorId = 1,
//                    text = "somethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomethingsomething"
//                )
//            )
//        }
//    }
//}