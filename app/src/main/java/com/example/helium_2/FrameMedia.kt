// UI КАРКАС МЕДИА

package com.example.helium_2

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import androidx.documentfile.provider.DocumentFile

import coil.compose.AsyncImage

import java.time.LocalDate


@Composable
fun FrameMedia(modifier: Modifier = Modifier) {
    val mediaFiles = viewModelApp.mediaFiles
    val mediaGroups = viewModelApp.mediaGroups

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp), columns = GridCells.Fixed(4)
    ) {
//        items(
//            items = mediaFiles.keys.sortedDescending().toList(),
//            key = { mediaDate -> mediaDate }) { mediaItem -> mediaFiles[mediaItem]?.let { MediaItem(it) } }

        mediaGroups.keys.sortedDescending().forEach { mediaGroup ->
            item(key = mediaGroup, span = { GridItemSpan(4) }) {
                MediaGroup(mediaGroup)
            }

            mediaGroups[mediaGroup]?.toSortedMap()?.forEach { (mediaDateTime, mediaItem) ->
                item(key = mediaDateTime) {
                    MediaItem(mediaItem)
                }
            }

            item(key = mediaGroup.toFormattedString(), span = { GridItemSpan(4) }) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun MediaGroup(mediaDate: LocalDate) {
    Text(
        modifier = Modifier.padding(vertical = 4.dp), text = mediaDate.toFormattedString()
    )
}


@Composable
fun MediaItem(mediaItem: DocumentFile) {
    AsyncImage(
        model = mediaItem.uri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .padding(1.dp)
    )
}
