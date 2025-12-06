// ПРОСМОТР МЕДИА

package com.example.helium_2

import android.content.Context
import android.content.Intent
import android.content.res.Configuration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Rotate90DegreesCw
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button

import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import coil.compose.AsyncImage


fun shareMedia(context: Context, mediaFile: MediaFile) {
    val shareFileIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, mediaFile.uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareFileIntent, null))
}

@Composable
fun FrameViewerCard(navController: NavController) {
    val folderCurrent by viewModelApp.folderCurrent
    val mediaFile by viewModelApp.mediaFile
    val mediaFiles = viewModelApp.mediaFiles
    val mediaKeys by remember { derivedStateOf { viewModelApp.mediaFiles.keys.toList().sortedDescending() } }
    var dialogDeleteMediaFileVisible by viewModelApp.dialogDeleteMediaFileVisible

    if (mediaFiles.isEmpty()) navController.navigate(SCREENS.FOLDER.screen) {
        popUpTo(0)
        launchSingleTop = true
    }

    val pagerState = rememberPagerState(
        initialPage = mediaKeys.indexOf(mediaFile?.fileTime),
        pageCount = { mediaFiles.count() })

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(state = pagerState) { page ->
            val pageFile = mediaFiles[mediaKeys[page]]

            if (dialogDeleteMediaFileVisible) {
                AlertDialog(
                    onDismissRequest = { dialogDeleteMediaFileVisible = false },
                    title = { Text("Удаление медиа-файла") },
                    text = { Text("${folderCurrent}\n${pageFile?.mediaTime}") },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModelApp.deleteMediaFile(pageFile)
                                dialogDeleteMediaFileVisible = false
                            }) {
                            Text("Удалить")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = { dialogDeleteMediaFileVisible = false }) {
                            Text("Оставить")
                        }
                    })
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                FrameViewerCardInfo(mediaFile = pageFile)
                FrameViewerCardMedia(mediaFile = pageFile, modifier = Modifier.weight(1.0f))
                FrameViewerCardTools(mediaFile = pageFile)
            }
        }
    }
}

@Composable
fun FrameViewerCardInfo(mediaFile: MediaFile?) {
    val folderCurrent by viewModelApp.folderCurrent
    var showDetails by viewModelApp.mediaViewVisibleDetails
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (showDetails and !isLandscape) {
        Card(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
        ) {
            if (mediaFile != null) {

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        text = folderCurrent,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = mediaFile.mediaTime,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun FrameViewerCardMedia(mediaFile: MediaFile?, modifier: Modifier) {
    var showDetails by viewModelApp.mediaViewVisibleDetails
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (showDetails and !isLandscape) {
        ElevatedCard(
            modifier = modifier
                .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
        ) {
            FrameViewerCardMediaImage(mediaFile = mediaFile)
        }
    } else {
        Box(
            modifier = modifier
                .background(color = Color.Black)
        ) {
            FrameViewerCardMediaImage(mediaFile = mediaFile)
        }
    }
}

@Composable
fun FrameViewerCardMediaImage(mediaFile: MediaFile?) {
    if (mediaFile == null) return

    var showDetails by viewModelApp.mediaViewVisibleDetails
    val mediaViewRotates = viewModelApp.mediaViewRotates
    val isHidden = mediaFile.isHidden

    val refreshHook by viewModelApp.refreshHook
    if (refreshHook < 0) return

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .graphicsLayer { rotationZ = (mediaViewRotates[mediaFile] ?: 0.0f) }
                .fillMaxSize()
                .background(color = Color.Black)
                .clickable { showDetails = !showDetails },
            model = mediaFile.uri,
            contentDescription = null,
            contentScale = ContentScale.Fit,

            )

        if (isHidden) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .alpha(0.5f),
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun FrameViewerCardTools(mediaFile: MediaFile?) {
    var showDetails by viewModelApp.mediaViewVisibleDetails
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current

    if (mediaFile == null) return
    if (!showDetails) return
    if (isLandscape) return

    val isHidden = mediaFile.isHidden

    val refreshHook by viewModelApp.refreshHook
    if (refreshHook < 0) return

    Card(
        modifier = Modifier
            .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { viewModelApp.rotateMediaToCw(mediaFile) }) {
                Icon(
                    imageVector = Icons.Default.Rotate90DegreesCw,
                    contentDescription = null
                )
            }

            IconButton(onClick = {
                shareMedia(
                    context = context,
                    mediaFile = mediaFile
                )
            }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }

            IconButton(onClick = { viewModelApp.switchVisibleMediaFile(mediaFile) }) {
                Icon(
                    imageVector = if (isHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null
                )
            }

            IconButton(onClick = { viewModelApp.dialogDeleteMediaFileVisible.value = true }) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null
                )
            }
        }
    }

}
