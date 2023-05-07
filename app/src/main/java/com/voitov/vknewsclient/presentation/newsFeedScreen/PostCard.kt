package com.voitov.vknewsclient.presentation.newsFeedScreen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.with
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.presentation.mainScreen.TAG
import com.voitov.vknewsclient.ui.theme.IconWithText
import com.voitov.vknewsclient.ui.theme.TransparentRed
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import kotlin.random.Random

private const val DELAY_BEFORE_START = 1000
private const val DELAY_BETWEEN_ANIMATIONS = 1200
private const val ANIMATION_DURATION = 3000
private const val INVISIBLE = 0F
private const val VISIBLE = 1F

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    postItem: PostItem,
    onCommentsClickListener: (SocialMetric) -> Unit,
    onLikesClickListener: (SocialMetric) -> Unit,
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

            val firstTimeFeedback = rememberSaveable {
                mutableStateOf(true)
            }

            LaunchedEffect(Unit) {
                firstTimeFeedback.value = false
            }

            AnimatedContent(targetState = firstTimeFeedback.value, transitionSpec = {
                slideIn(
                    animationSpec = tween(durationMillis = 3000),
                    initialOffset = { containerSize ->
                        IntOffset(
                            -containerSize.width / 15,
                            -containerSize.height / 3
                        )
                    },
                ) with fadeOut(animationSpec = tween(durationMillis = 500))
            }
            ) {
                PostFeedback(
                    postItem.isLikedByUser,
                    postItem.metrics,
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
    onCommentsClickListener: (SocialMetric) -> Unit,
    onLikesClickListener: (SocialMetric) -> Unit,
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
        val animatedFadeInViewsCount =
            animateFloatAsState(
                targetValue = if (firstTimeShownInNewsFeed.value) INVISIBLE else VISIBLE,
                animationSpec = tween(
                    ANIMATION_DURATION + DELAY_BETWEEN_ANIMATIONS * 0,
                    delayMillis = DELAY_BEFORE_START,
                    easing = LinearOutSlowInEasing
                ),
            )

        Row(modifier = Modifier.weight(0.3f)) {
            val views = metrics.getMetricByType(MetricsType.VIEWS)
            IconWithText(
                pictResId = R.drawable.ic_views_count,
                text = shortenLengthOfMetricsIfPossible(views.count),
                modifier = Modifier.alpha(animatedFadeInViewsCount.value)
            )
        }

        val animatedFadeInShares =
            animateFloatAsState(
                targetValue = if (firstTimeShownInNewsFeed.value) INVISIBLE else VISIBLE,
                animationSpec = tween(
                    ANIMATION_DURATION + DELAY_BETWEEN_ANIMATIONS * 1,
                    delayMillis = DELAY_BEFORE_START,
                    easing = LinearOutSlowInEasing
                ),
            )

        val animatedFadeInComments =
            animateFloatAsState(
                targetValue = if (firstTimeShownInNewsFeed.value) INVISIBLE else VISIBLE,
                animationSpec = tween(
                    ANIMATION_DURATION + DELAY_BETWEEN_ANIMATIONS * 2,
                    delayMillis = DELAY_BEFORE_START,
                    easing = LinearOutSlowInEasing
                ),
            )

        val animatedFadeInLikes =
            animateFloatAsState(
                targetValue = if (firstTimeShownInNewsFeed.value) INVISIBLE else VISIBLE,
                animationSpec = tween(
                    ANIMATION_DURATION + DELAY_BETWEEN_ANIMATIONS * 3,
                    delayMillis = DELAY_BEFORE_START,
                    easing = LinearOutSlowInEasing
                ),
            )

        Row(
            modifier = Modifier.weight(0.70f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            with(metrics.getMetricByType(MetricsType.SHARES)) {
                IconWithText(
                    pictResId = R.drawable.ic_share,
                    text = count.toString(),
                    modifier = Modifier.alpha(animatedFadeInShares.value)
                )
            }
            with(metrics.getMetricByType(MetricsType.COMMENTS)) {
                IconWithText(
                    pictResId = R.drawable.ic_comment,
                    text = count.toString(),
                    modifier = Modifier.alpha(animatedFadeInComments.value)
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
                        TransparentRed
                    } else {
                        MaterialTheme.colors.onSecondary
                    },
                    modifier = Modifier.alpha(animatedFadeInLikes.value)
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

@Preview
@Composable
fun NewsPostLightDark() {
    VkNewsClientTheme(darkTheme = true) {
        PostCard(
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