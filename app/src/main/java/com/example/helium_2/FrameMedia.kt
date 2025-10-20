// UI КАРКАС МЕДИА

package com.example.helium_2

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FrameMedia(modifier: Modifier = Modifier) {
    val mediaDates = viewModelApp.mediaDates

    LazyColumn(
        modifier = modifier.padding(8.dp).fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(mediaDates) { mediaDate ->
            Text(":: $mediaDate")
        }
    }
}
