package com.voitov.vknewsclient.presentation.reusableUIs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.ui.theme.CoolBlack
import com.voitov.vknewsclient.ui.theme.VkBlue
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun IconedChip(
    isSelected: Boolean,
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier,
    onClick: ((Boolean) -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .background(
                color = if (isSystemInDarkTheme()) {
                    if (isSelected) {
                        MaterialTheme.colors.onPrimary
                    } else {
                        MaterialTheme.colors.primary
                    }
                } else {
                    if (isSelected) {
                        MaterialTheme.colors.onPrimary
                    } else {
                        MaterialTheme.colors.primary
                    }
                }
            )
            .border(
                width = 2.dp,
                color = if (isSystemInDarkTheme()) {
                    if (isSelected) {
                        Color.White
                    } else {
                        VkBlue
                    }
                } else {
                    CoolBlack
                },
                shape = CircleShape
            )
            .padding(
                vertical = 6.dp,
                horizontal = 8.dp
            )
            .clickable {
                onClick?.invoke(!isSelected)
            }
    ) {
        if (isSelected) {
            val icon = if (isSystemInDarkTheme()) {
                painterResource(id = R.drawable.ic_check_black)
            } else {
                painterResource(id = R.drawable.ic_check_white)
            }
            Image(painter = icon, contentDescription = null)
        }
        Text(
            text = text,
            color = if (isSystemInDarkTheme()) {
                if (isSelected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onPrimary
                }
            } else {
                if (isSelected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onPrimary
                }
            }
        )
    }
}

@Preview
@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun PreviewSelectedChip() {
    VkNewsClientTheme {
        IconedChip(
            isSelected = true,
            text = "Belarus",
            painter = painterResource(id = R.drawable.ic_check_white)
        )
    }
}