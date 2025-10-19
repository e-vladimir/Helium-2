// UI КАРКАС МЕДИА

package com.example.helium_2

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FrameMedia(modifier: Modifier = Modifier) {
    val debugData = viewModelApp.debugData

    Text(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        text = "DEBUG\n" + debugData.value
    )
}
