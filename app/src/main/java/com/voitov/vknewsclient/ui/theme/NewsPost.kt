package com.voitov.vknewsclient.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.voitov.vknewsclient.ui.theme.domain.MetricsType
import com.voitov.vknewsclient.ui.theme.domain.PostItem
import com.voitov.vknewsclient.ui.theme.domain.SocialMetric

@Composable
fun NewsPost(
    modifier: Modifier = Modifier,
    postItem: PostItem,
    onFeedbackItemClickListener: (SocialMetric) -> Unit
) {
    Log.d(TAG, "NewsPost")

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(all = 8.dp)
        ) {
            PostHeader(postItem)
            PostContent(postItem)
            PostFeedback(
                postItem.metrics
            ) {
                onFeedbackItemClickListener(it)
            }
        }
    }
}

@Composable
private fun PostHeader(postItem: PostItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp),
            painter = painterResource(id = postItem.avatarResId),
            contentDescription = "community thumbnail"
        )
        Spacer(modifier = Modifier.padding(all = 8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(postItem.authorName, color = MaterialTheme.colors.onPrimary)
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            Text(postItem.time, color = MaterialTheme.colors.onSecondary)
        }
        Image(
            imageVector = Icons.Rounded.MoreVert,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentDescription = "list of actions"
        )
    }
}

@Composable
private fun PostContent(postItem: PostItem) {
    PostText(postItem)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    PostAdditionalResources(postItem)
}

@Composable
private fun PostText(postItem: PostItem) {
    Text(
        text = stringResource(postItem.contentTextResId),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun PostAdditionalResources(postItem: PostItem) {
    Image(
        modifier = Modifier
            .fillMaxWidth(),
        painter = painterResource(postItem.contentImagesResId),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
private fun PostFeedback(
    metrics: List<SocialMetric>,
    onItemClickListener: (SocialMetric) -> Unit,
) {
    Log.d(TAG, "PostFeedback")
    Row(
        modifier = Modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f)) {
            val views = metrics.getMetricByType(MetricsType.VIEWS)
            IconWithText(pictResId = R.drawable.ic_views_count, text = views.count.toString()) {
                onItemClickListener(views)
            }
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            with(metrics.getMetricByType(MetricsType.SHARES)) {
                IconWithText(
                    pictResId = R.drawable.ic_share,
                    text = count.toString()
                ) {
                    onItemClickListener(this@with)
                }
            }
            with(metrics.getMetricByType(MetricsType.COMMENTS)) {
                IconWithText(
                    pictResId = R.drawable.ic_comment,
                    text = count.toString()
                ) {
                    onItemClickListener(this@with)
                }
            }
            with(metrics.getMetricByType(MetricsType.LIKES)) {
                IconWithText(pictResId = R.drawable.ic_like, text = count.toString())
                {
                    onItemClickListener(this@with)
                }
            }
        }
    }
}

fun List<SocialMetric>.getMetricByType(type: MetricsType): SocialMetric {
    return this.find { it.type == type } ?: throw IllegalStateException()
}

@Composable
private fun IconWithText(pictResId: Int, text: String, onItemClickListener: () -> Unit) {
    Row(
        modifier = Modifier.clickable {
            onItemClickListener()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        NewsPost(postItem = PostItem()) {

        }
    }
}

@Preview
@Composable
fun NewsPostLightDark() {
    VkNewsClientTheme(darkTheme = true) {
        NewsPost(postItem = PostItem()) {

        }
    }
}