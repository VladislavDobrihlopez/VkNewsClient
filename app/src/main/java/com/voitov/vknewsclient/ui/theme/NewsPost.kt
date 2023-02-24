package com.voitov.vknewsclient.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R

@Composable
fun NewsPost() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .padding(all = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
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