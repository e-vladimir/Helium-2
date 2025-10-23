// UI КАРКАС МЕДИА

package com.example.helium_2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier


@Composable
fun FrameMedia(modifier: Modifier = Modifier) {
    val dataDebug by viewModelApp.dataDebug

    Text(
        modifier = modifier.fillMaxSize(),
        text = dataDebug
    )
}


@Composable
fun MediaGroup() {
}


@Composable
fun MediaItem() {
}
