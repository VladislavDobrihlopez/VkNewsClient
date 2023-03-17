package com.voitov.vknewsclient.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconWithText(
    pictResId: Int,
    text: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colors.onSecondary,
    onItemClickListener: (() -> Unit)? = null,
) {
    val rowModifier = if (onItemClickListener == null) {
        modifier.clickable(enabled = false) {}
    } else {
        modifier.clickable {
            onItemClickListener()
        }
    }
    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(pictResId),
            contentDescription = "",
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = text,
            color = MaterialTheme.colors.onSecondary
        )
        Spacer(modifier = Modifier.width(2.dp))
    }
}