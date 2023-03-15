package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.presentation.mainScreen.TAG
import com.voitov.vknewsclient.ui.theme.IconWithText
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import kotlin.random.Random

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    postItem: PostItem,
    onSharesClickListener: (SocialMetric) -> Unit,
    onCommentsClickListener: (SocialMetric) -> Unit,
    onLikesClickListener: (SocialMetric) -> Unit,
    onViewsClickListener: (SocialMetric) -> Unit,
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
                postItem.isLiked,
                postItem.metrics,
                onViewsClickListener = {
                    onViewsClickListener(it)
                },
                onSharesClickListener = {
                    onSharesClickListener(it)
                },
                onCommentsClickListener = {
                    onCommentsClickListener(it)
                },
                onLikesClickListener = {
                    onLikesClickListener(it)
                },
            )
        }
    }
}

@Composable
private fun PostHeader(postItem: PostItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = postItem.communityPhotoUrl,
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp),
            contentDescription = "community thumbnail"
        )
        Spacer(modifier = Modifier.padding(all = 8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(postItem.authorName, color = MaterialTheme.colors.onPrimary)
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            Text(postItem.date, color = MaterialTheme.colors.onSecondary)
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
        text = postItem.contentText,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun PostAdditionalResources(postItem: PostItem) {
    AsyncImage(
        model = postItem.contentImageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
private fun PostFeedback(
    isPostLiked: Boolean,
    metrics: List<SocialMetric>,
    onSharesClickListener: (SocialMetric) -> Unit,
    onCommentsClickListener: (SocialMetric) -> Unit,
    onLikesClickListener: (SocialMetric) -> Unit,
    onViewsClickListener: (SocialMetric) -> Unit,
) {
    Log.d(TAG, "PostFeedback")
    Row(
        modifier = Modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(0.3f)) {
            val views = metrics.getMetricByType(MetricsType.VIEWS)
            IconWithText(
                pictResId = R.drawable.ic_views_count,
                text = shortenLengthOfMetricsIfPossible(views.count)
            ) {
                onViewsClickListener(views)
            }
        }
        Row(
            modifier = Modifier.weight(0.70f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            with(metrics.getMetricByType(MetricsType.SHARES)) {
                IconWithText(
                    pictResId = R.drawable.ic_share,
                    text = count.toString(),
                ) {
                    onSharesClickListener(this@with)
                }
            }
            with(metrics.getMetricByType(MetricsType.COMMENTS)) {
                IconWithText(
                    pictResId = R.drawable.ic_comment,
                    text = count.toString()
                ) {
                    onCommentsClickListener(this@with)
                }
            }
            with(metrics.getMetricByType(MetricsType.LIKES)) {
                IconWithText(
                    pictResId = if (isPostLiked) {
                        R.drawable.ic_liked
                    } else {
                        R.drawable.ic_like
                    },
                    text = shortenLengthOfMetricsIfPossible(count),
                    iconTint = if (isPostLiked) {
                        Color.Red
                    } else {
                        MaterialTheme.colors.onSecondary
                    }
                ) {
                    onLikesClickListener(this@with)
                }
            }
        }
    }
}

private fun shortenLengthOfMetricsIfPossible(count: Int): String {
    return if (count >= 1_000_000) {
        String.format("%.1f", (count / 1000f)) + "M"
    } else if (count >= 100_000) {
        "${count.toDouble() / 1000}K"
    } else if (count >= 1000) {
        String.format("%.1f", (count / 1000f)) + "K"
    } else {
        count.toString()
    }
}

fun List<SocialMetric>.getMetricByType(type: MetricsType): SocialMetric {
    return this.find { it.type == type } ?: throw IllegalStateException()
}

@Preview
@Composable
fun NewsPostLightTheme() {
    VkNewsClientTheme(darkTheme = false) {
        PostCard(
            postItem = PostItem(
                id = "1",
                R.drawable.post_community_image.toString(),
                "Maks Korzh",
                "today",
                contentText = stringResource(
                    id = R.string.post_text
                ),
                contentImageUrl = null,
                isLiked = Random.nextBoolean(),
                metrics = listOf(
                    SocialMetric(MetricsType.LIKES, 2_100),
                    SocialMetric(MetricsType.VIEWS, 15_000),
                    SocialMetric(MetricsType.COMMENTS, 7_000),
                    SocialMetric(MetricsType.SHARES, 5_000),
                ),
            ),
            onViewsClickListener = {},
            onSharesClickListener = {},
            onCommentsClickListener = {},
            onLikesClickListener = {}
        )
    }
}

@Preview
@Composable
fun NewsPostLightDark() {
    VkNewsClientTheme(darkTheme = true) {
        PostCard(
            postItem = PostItem(
                id = "1",
                R.drawable.post_community_image.toString(),
                "Maks Korzh",
                "today",
                contentText = stringResource(
                    id = R.string.post_text
                ),
                contentImageUrl = null,
                isLiked = Random.nextBoolean(),
                metrics = listOf(
                    SocialMetric(MetricsType.LIKES, 2_100),
                    SocialMetric(MetricsType.VIEWS, 15_000),
                    SocialMetric(MetricsType.COMMENTS, 7_000),
                    SocialMetric(MetricsType.SHARES, 5_000),
                )
            ),
            onViewsClickListener = {},
            onSharesClickListener = {},
            onCommentsClickListener = {},
            onLikesClickListener = {}
        )
    }
}