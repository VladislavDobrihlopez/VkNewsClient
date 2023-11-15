package com.voitov.vknewsclient.presentation.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.voitov.vknewsclient.presentation.util.shortenLengthOfMetricsIfPossible
import com.voitov.vknewsclient.ui.theme.Shapes
import com.voitov.vknewsclient.ui.theme.TransparentBlue
import com.voitov.vknewsclient.ui.theme.TransparentRed
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import kotlin.math.min
import kotlin.random.Random

private const val DELAY_BEFORE_START = 900
private const val DELAY_BETWEEN_ANIMATIONS = 1500
private const val ANIMATION_DURATION = 1200
private const val INVISIBLE = 0F
private const val VISIBLE = 1F

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    postItem: PostItem,
    PostHeaderContent: @Composable (ColumnScope.() -> Unit)? = null,
    PostContent: @Composable (ColumnScope.() -> Unit)? = null,
    PostFeedbackContent: @Composable (ColumnScope.() -> Unit)? = null,
    onCommentsClickListener: ((SocialMetric) -> Unit)? = null,
    onLikesClickListener: ((SocialMetric) -> Unit)? = null,
    onSharesClickListener: ((SocialMetric) -> Unit)? = null
) {
    Card(
        shape = Shapes.medium,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(all = 8.dp)
        ) {
            PostHeaderContent?.invoke(this) ?: PostHeader(postItem = postItem)
            PostContent?.invoke(this) ?: PostContent(postItem = postItem)
            PostFeedbackContent?.invoke(this) ?: PostFeedback(
                isPostShared = postItem.isSharedByUser,
                isPostLiked = postItem.isLikedByUser,
                metrics = postItem.metrics,
                onCommentsClickListener = {
                    onCommentsClickListener?.invoke(it)
                },
                onLikesClickListener = {
                    onLikesClickListener?.invoke(it)
                },
                onSharesClickListener = {
                    onSharesClickListener?.invoke(it)
                }
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
    PostText(postItem.contentText)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    PostAdditionalPhotos(postItem.contentImageUrl)
}

@Composable
fun PostText(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostAdditionalPhotos(contentImgUrls: List<String>) {
    val pagerState = rememberPagerState()

    val pagesCount = contentImgUrls.size

    HorizontalPager(
        pageCount = pagesCount,
        beyondBoundsPageCount = min(contentImgUrls.size, 2),
        state = pagerState
    ) { page ->
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            model = contentImgUrls[page],
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
    }

    if (pagesCount > 1) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagesCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration)
                        if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                    else
                        if (!isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
fun PostFeedback(
    isPostLiked: Boolean,
    isPostShared: Boolean,
    metrics: List<SocialMetric>,
    onCommentsClickListener: ((SocialMetric) -> Unit)? = null,
    onLikesClickListener: ((SocialMetric) -> Unit)? = null,
    onSharesClickListener: ((SocialMetric) -> Unit)? = null
) {
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
                    easing = FastOutLinearInEasing
                ),
            )
        val animatedFadeInShares =
            animateFloatAsState(
                targetValue = if (firstTimeShownInNewsFeed.value) INVISIBLE else VISIBLE,
                animationSpec = tween(
                    ANIMATION_DURATION + DELAY_BETWEEN_ANIMATIONS * 1,
                    delayMillis = DELAY_BEFORE_START,
                    easing = FastOutLinearInEasing
                ),
            )

        val animatedFadeInComments =
            animateFloatAsState(
                targetValue = if (firstTimeShownInNewsFeed.value) INVISIBLE else VISIBLE,
                animationSpec = tween(
                    ANIMATION_DURATION + DELAY_BETWEEN_ANIMATIONS * 2,
                    delayMillis = DELAY_BEFORE_START,
                    easing = FastOutLinearInEasing
                ),
            )

        val animatedFadeInLikes =
            animateFloatAsState(
                targetValue = if (firstTimeShownInNewsFeed.value) INVISIBLE else VISIBLE,
                animationSpec = tween(
                    ANIMATION_DURATION + DELAY_BETWEEN_ANIMATIONS * 3,
                    delayMillis = DELAY_BEFORE_START,
                    easing = FastOutLinearInEasing
                ),
            )
        Row(modifier = Modifier.weight(0.3f)) {
            AnimatedViewsCount(
                animatedFadeInViewsCount,
                metrics.getMetricByType(MetricsType.VIEWS).count,
            )
        }
        Log.d("TEST_ANIMATIONS", "recomposition")
        Row(
            modifier = Modifier.weight(0.70f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AnimatedSharesCount(
                animatedFadeInShares,
                metrics.getMetricByType(MetricsType.SHARES).count,
                isPostShared
            ) {
                onSharesClickListener?.invoke(metrics.getMetricByType(MetricsType.SHARES))
            }
            AnimatedCommentsCount(
                animatedFadeInComments,
                metrics.getMetricByType(MetricsType.COMMENTS).count
            ) {
                onCommentsClickListener?.invoke(metrics.getMetricByType(MetricsType.COMMENTS))
            }
            AnimatedLikesCount(
                animatedFadeInLikes,
                metrics.getMetricByType(MetricsType.LIKES).count,
                isPostLiked
            ) {
                onLikesClickListener?.invoke(metrics.getMetricByType(MetricsType.LIKES))
            }
        }
    }
}

@Composable
private fun AnimatedViewsCount(
    alpha: State<Float>,
    views: Int,
) {
    IconFollowedByText(
        pictResId = R.drawable.ic_views_count,
        text = shortenLengthOfMetricsIfPossible(views),
        modifier = Modifier.alpha(alpha.value),
    )
}

@Composable
private fun AnimatedSharesCount(
    alpha: State<Float>,
    shares: Int,
    isPostShared: Boolean,
    onItemClicked: (() -> Unit)? = null
) {
    IconFollowedByText(
        pictResId = if (isPostShared) {
            R.drawable.ic_shared
        } else {
            R.drawable.ic_share
        },
        iconTint = if (isPostShared) {
            TransparentBlue
        } else {
            MaterialTheme.colors.onSecondary
        },
        text = shares.toString(),
        modifier = Modifier.alpha(alpha.value)
    ) {
        onItemClicked?.invoke()
    }
}

@Composable
private fun AnimatedCommentsCount(
    alpha: State<Float>,
    comments: Int,
    onItemClicked: (() -> Unit)? = null
) {
    IconFollowedByText(
        pictResId = R.drawable.ic_comment,
        text = comments.toString(),
        modifier = Modifier.alpha(alpha.value)
    ) {
        onItemClicked?.invoke()
    }
}

@Composable
private fun AnimatedLikesCount(
    alpha: State<Float>,
    likes: Int,
    isPostLiked: Boolean,
    onItemClicked: (() -> Unit)? = null
) {
    IconFollowedByText(
        pictResId = if (isPostLiked) {
            R.drawable.ic_liked
        } else {
            R.drawable.ic_like
        },
        text = shortenLengthOfMetricsIfPossible(likes),
        iconTint = if (isPostLiked) {
            TransparentRed
        } else {
            MaterialTheme.colors.onSecondary
        },
        modifier = Modifier.alpha(alpha.value)
    ) {
        onItemClicked?.invoke()
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
        PostCard(
            postItem = PostItem(
                id = 1,
                communityId = 1,
                R.drawable.post_community_image.toString(),
                "Maks Korzh",
                date = "3132",
                dateInMillis = 23321321,
                contentText = stringResource(
                    id = R.string.post_text
                ),
                contentImageUrl = listOf(),
                isLikedByUser = Random.nextBoolean(),
                isSharedByUser = Random.nextBoolean(),
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
        PostCard(
            postItem = PostItem(
                id = 1,
                communityId = 1,
                R.drawable.post_community_image.toString(),
                "Maks Korzh",
                date = "3132",
                dateInMillis = 23321321,
                contentText = stringResource(
                    id = R.string.post_text
                ),
                contentImageUrl = listOf(),
                isLikedByUser = Random.nextBoolean(),
                isSharedByUser = Random.nextBoolean(),
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