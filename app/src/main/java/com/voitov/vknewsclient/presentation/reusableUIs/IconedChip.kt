package com.voitov.vknewsclient.presentation.reusableUIs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voitov.vknewsclient.R
import com.voitov.vknewsclient.ui.theme.CoolBlack
import com.voitov.vknewsclient.ui.theme.VkBlue
import com.voitov.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun IconedChip(
    enabled: Boolean,
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    borderWidth: Dp? = null,
    onClick: ((Boolean) -> Unit)? = null,
    iconPart: (@Composable RowScope.() -> Unit)? = null,
    textPart: (@Composable RowScope.() -> Unit)? = null,
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
                width = borderWidth ?: 2.dp,
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
            .clickable(enabled = enabled) {
                onClick?.invoke(!isSelected)
            }
    ) {
        if (isSelected) {
            iconPart?.invoke(this) ?: Image(
                painter = if (isSystemInDarkTheme()) {
                    painterResource(id = R.drawable.ic_check_black)
                } else {
                    painterResource(id = R.drawable.ic_check_white)
                }, contentDescription = null
            )
        }
        textPart?.invoke(this) ?: Text(
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
            enabled = true,
            isSelected = true,
            text = "Belarus",
            painter = painterResource(id = R.drawable.ic_check_white)
        )
    }
}