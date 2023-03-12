package com.voitov.vknewsclient.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Preview
@Composable
fun TestActivityResult() {
    val imageUriState = rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            imageUriState.value = it
        }
    )

    Column(
        modifier = Modifier
            .padding(8.dp)
            .padding(bottom = 60.dp)
            .fillMaxSize()
    ) {
        AsyncImage(
            modifier = Modifier.weight(1f),
            model = imageUriState.value,
            contentDescription = null
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onClick = {
                launcher.launch("image/*")
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                backgroundColor = Color.Blue
            )
        ) {
            Text(text = "Get image")
        }
    }
}