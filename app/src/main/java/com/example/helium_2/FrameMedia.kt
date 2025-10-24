// UI КАРКАС МЕДИА

package com.example.helium_2

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import java.time.LocalDate


@Composable
fun FrameMedia(modifier: Modifier = Modifier) {
    val mediaDates = viewModelApp.mediaDates

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        columns = GridCells.Fixed(4)
    ) {
        mediaDates.forEach { mediaDate ->
            item(
                key = mediaDate.toFormattedString(),
                span = { GridItemSpan(4) }
            )
            {
                MediaGroup(mediaDate)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun MediaGroup(mediaDate: LocalDate) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = mediaDate.toFormattedString()
    )
}


@Composable
fun MediaItem() {
}
