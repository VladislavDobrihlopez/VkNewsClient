package com.voitov.vknewsclient.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R

@Composable
fun NewsPost() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(all = 8.dp)
        ) {
            PostHeader()
            PostContent()
            PostFeedback()
        }
    }
}

@Composable
private fun PostHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp),
            painter = painterResource(id = R.drawable.post_community_image),
            contentDescription = "community thumbnail"
        )
        Spacer(modifier = Modifier.padding(all = 8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("Max Korzh", color = MaterialTheme.colors.onPrimary)
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            Text("12:00", color = MaterialTheme.colors.onSecondary)
        }
        Image(
            imageVector = Icons.Rounded.MoreVert,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentDescription = "list of actions"
        )
    }
}

@Composable
private fun PostContent() {
    PostText()
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    PostAdditionalResources()
}

@Composable
private fun PostText() {
    Text(
        text = stringResource(R.string.post_text),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun PostAdditionalResources() {
    Image(
        modifier = Modifier
            .fillMaxWidth(),
        painter = painterResource(R.drawable.post_content_image),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
private fun PostFeedback() {
    Row(
        modifier = Modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f)) {
            IconWithText(pictResId = R.drawable.ic_views_count, text = "100k")
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconWithText(pictResId = R.drawable.ic_share, text = "25")
            IconWithText(pictResId = R.drawable.ic_comment, text = "150")
            IconWithText(pictResId = R.drawable.ic_like, text = "250")
        }
    }
}

@Composable
private fun IconWithText(pictResId: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(pictResId),
            contentDescription = "",
            tint = MaterialTheme.colors.onSecondary
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = text,
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Preview
@Composable
fun NewsPostLightTheme() {
    VkNewsClientTheme(darkTheme = false) {
        NewsPost()
    }
}

@Preview
@Composable
fun NewsPostLightDark() {
    VkNewsClientTheme(darkTheme = true) {
        NewsPost()
    }
}