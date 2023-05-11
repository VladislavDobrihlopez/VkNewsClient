package com.voitov.vknewsclient.presentation.favoritePostsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.domain.MetricsType
import com.voitov.vknewsclient.domain.SocialMetric
import com.voitov.vknewsclient.domain.entities.PostItem
import com.voitov.vknewsclient.presentation.reusableUIs.IconedChip
import com.voitov.vknewsclient.ui.theme.Shapes
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme
import kotlin.random.Random

@Composable
fun FavoritePostsScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary,
            shape = Shapes.medium
        ) {
            Tags(modifier = Modifier.padding(4.dp))
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(10) {
                SavedPostCard(postItem = PostItem(
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
                    onLikesClickListener = {})
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Tags(modifier: Modifier = Modifier) {
    val filters =
        listOf("Washer/Dryer", "Ramp access", "Garden", "Cats OK", "Dogs OK", "Smoke-free")
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        maxItemsInEachRow = 7
    ) {
        filters.forEach {
            val selected = remember { mutableStateOf(false) }
            Box(modifier = Modifier.padding(vertical = 4.dp)) {
                IconedChip(
                    isSelected = !selected.value,
                    onClick = { selected.value = !selected.value },
                    painter = if (isSystemInDarkTheme())
                        painterResource(id = R.drawable.ic_check_white)
                    else
                        painterResource(
                            id = R.drawable.ic_check_black
                        ),
                    text = it
                )
                IconedChip(
                    isSelected = !selected.value,
                    onClick = { selected.value = !selected.value },
                    painter = if (isSystemInDarkTheme())
                        painterResource(
                            id = R.drawable.ic_check_black
                        )
                    else
                        painterResource(
                            id = R.drawable.ic_check_white
                        ),
                    text = it
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        }
    }
}

@Preview
@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun PreviewFavoritePostsScreen() {
    VkNewsClientTheme(darkTheme = true) {
        FavoritePostsScreen()
    }
}