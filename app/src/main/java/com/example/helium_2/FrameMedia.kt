// ГАЛЕРЕЯ

package com.example.helium_2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController

import coil.compose.AsyncImage

import java.time.LocalDateTime


@Composable
fun FrameMedia(modifier: Modifier = Modifier, navController: NavController) {
    val mediaGroups = viewModelApp.mediaGroups

    LazyColumn(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        mediaGroups.keys.sortedDescending().forEach { mediaGroup ->
            stickyHeader {
                MediaGroupHeader(
                    mediaGroup = mediaGroup.toFormattedString()
                )
            }

            item(key = mediaGroup.toFormattedString()) {
                MediaGroup(
                    mediaFiles = mediaGroups[mediaGroup] ?: emptyMap(), navController = navController
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun MediaGroupHeader(mediaGroup: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.90f))
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = mediaGroup,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}


@Composable
fun MediaGroup(mediaFiles: Map<LocalDateTime, MediaFile>, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        MediaGrid(
            mediaFiles = mediaFiles,
            navController = navController
        )
    }
}


@Composable
fun MediaGrid(mediaFiles: Map<LocalDateTime, MediaFile>, navController: NavController) {
    Column(modifier = Modifier.padding(8.dp)) {
        mediaFiles.keys.sortedDescending().chunked(4).forEach { mediaTimes ->
            Row {
                mediaTimes.forEach { mediaTime ->
                    MediaItem(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        mediaFile = mediaFiles[mediaTime]!!,
                        navController = navController
                    )
                }

                repeat(4 - mediaTimes.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        }
    }
}


@Composable
fun MediaItem(modifier: Modifier, mediaFile: MediaFile, navController: NavController) {
    val context = LocalContext.current

    AsyncImage(
        model = mediaFile.uri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .padding(1.dp)
            .alpha(if (mediaFile.isHidden) 0.20f else 1.00f)
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                mediaFile.readSize(context)

                viewModelApp.mediaFile.value = mediaFile
                navController.navigate(SCREENS.MEDIA.screen) {
                    launchSingleTop = true
                }
            })
}
