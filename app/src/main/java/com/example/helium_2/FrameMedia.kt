// ГАЛЕРЕЯ

package com.example.helium_2

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController

import coil.compose.AsyncImage

import java.time.LocalDate
import java.util.Comparator


@Composable
fun FrameMedia(modifier: Modifier = Modifier, navController: NavController) {
    val mediaGroups = viewModelApp.mediaGroups

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp), columns = GridCells.Fixed(4)
    ) {
        mediaGroups.keys.sortedDescending().forEach { mediaGroup ->
            item(key = mediaGroup, span = { GridItemSpan(4) }) {
                MediaGroup(mediaGroup)
            }

            mediaGroups[mediaGroup]?.toSortedMap(Comparator.reverseOrder())
                ?.forEach { (mediaDateTime, mediaFile) ->
                    item(key = mediaDateTime) {
                        MediaItem(
                            mediaFile = mediaFile,
                            navController = navController
                        )
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
fun MediaItem(mediaFile: MediaFile, navController: NavController) {
    AsyncImage(
        model = mediaFile.uri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .border(1.dp, MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable {
                viewModelApp.mediaFile.value = mediaFile
                navController.navigate(SCREENS.MEDIA.screen)
            }
    )
}
