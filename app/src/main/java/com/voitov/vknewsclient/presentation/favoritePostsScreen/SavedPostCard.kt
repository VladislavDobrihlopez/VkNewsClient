package com.voitov.vknewsclient.presentation.favoritePostsScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.voitov.vknewsclient.presentation.util.shortenLengthOfMetricsIfPossible
import com.voitov.vknewsclient.ui.theme.IconWithText
import com.voitov.vknewsclient.ui.theme.Shapes
import com.voitov.vknewsclient.ui.theme.TransparentRed
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import kotlin.random.Random

@Composable
fun SavedPostCard(
    modifier: Modifier = Modifier,
    postItem: PostItem,
    onCommentsClickListener: ((SocialMetric) -> Unit)? = null,
    onLikesClickListener: ((SocialMetric) -> Unit)? = null,
) {
    Log.d(TAG, "NewsPost")

    Card(
        shape = Shapes.medium,
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
                postItem.isLikedByUser,
                postItem.metrics,
                onCommentsClickListener = {
                    onCommentsClickListener?.invoke(it)
                },
                onLikesClickListener = {
                    onLikesClickListener?.invoke(it)
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
    onCommentsClickListener: ((SocialMetric) -> Unit)? = null,
    onLikesClickListener: ((SocialMetric) -> Unit)? = null,
) {
    Log.d(TAG, "PostFeedback")

    val firstTimeShownInNewsFeed = rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        firstTimeShownInNewsFeed.value = false
    }
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
            )
        }

        Row(
            modifier = Modifier.weight(0.70f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            with(metrics.getMetricByType(MetricsType.SHARES)) {
                IconWithText(
                    pictResId = R.drawable.ic_share,
                    text = count.toString(),
                )
            }
            with(metrics.getMetricByType(MetricsType.COMMENTS)) {
                IconWithText(
                    pictResId = R.drawable.ic_comment,
                    text = count.toString(),
                ) {
                    onCommentsClickListener?.invoke(this@with)
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
                        TransparentRed
                    } else {
                        MaterialTheme.colors.onSecondary
                    },
                ) {
                    onLikesClickListener?.invoke(this@with)
                }
            }
        }
    }
}

fun List<SocialMetric>.getMetricByType(type: MetricsType): SocialMetric {
    return this.find { it.type == type } ?: throw IllegalStateException()
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview
@Composable
fun NewsPostLightTheme() {
    VkNewsClientTheme(darkTheme = false) {
        SavedPostCard(
            postItem = PostItem(
                id = 1,
                communityId = 1,
                R.drawable.post_community_image.toString(),
                "Maks Korzh",
                "today",
                contentText = stringResource(
                    id = R.string.post_text
                ),
                contentImageUrl = null,
                isLikedByUser = Random.nextBoolean(),
                metrics = listOf(
                    SocialMetric(MetricsType.LIKES, 2_100),
                    SocialMetric(MetricsType.VIEWS, 15_000),
                    SocialMetric(MetricsType.COMMENTS, 7_000),
                    SocialMetric(MetricsType.SHARES, 5_000),
                ),
            ),
            onCommentsClickListener = {},
            onLikesClickListener = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview
@Composable
fun NewsPostLightDark() {
    VkNewsClientTheme(darkTheme = true) {
        SavedPostCard(
            postItem = PostItem(
                id = 1,
                communityId = 1,
                R.drawable.post_community_image.toString(),
                "Maks Korzh",
                "today",
                contentText = stringResource(
                    id = R.string.post_text
                ),
                contentImageUrl = null,
                isLikedByUser = Random.nextBoolean(),
                metrics = listOf(
                    SocialMetric(MetricsType.LIKES, 2_100),
                    SocialMetric(MetricsType.VIEWS, 15_000),
                    SocialMetric(MetricsType.COMMENTS, 7_000),
                    SocialMetric(MetricsType.SHARES, 5_000),
                )
            ),
            onCommentsClickListener = {},
            onLikesClickListener = {}
        )
    }
}