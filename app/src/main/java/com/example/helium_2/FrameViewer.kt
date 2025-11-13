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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Rotate90DegreesCw
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile

import coil.compose.AsyncImage


fun shareMedia(context: Context, mediaFile: DocumentFile) {
    val shareFileIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, mediaFile.uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareFileIntent, null))
}

@Composable
fun FrameViewerCard() {
    val mediaFiles = viewModelApp.mediaFiles
    val mediaKeys = mediaFiles.keys.toList().sortedDescending()
    val mediaFile by viewModelApp.mediaFile

    val pagerState = rememberPagerState(
        initialPage = mediaKeys.indexOf(
            mediaFile?.lastModified()?.toLocalDateTime()
        ), pageCount = { mediaFiles.count() })

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(state = pagerState) { page ->
            val pageFile = mediaFiles[mediaKeys[page]]

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
fun FrameViewerCardInfo(mediaFile: DocumentFile?) {
    val folderCurrent by viewModelApp.folderCurrent
    var showDetails by viewModelApp.mediaViewDetails
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (showDetails and !isLandscape) {
        Card(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
        ) {
            if (mediaFile != null) {

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        text = folderCurrent,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = mediaFile.lastModified()
                            .toLocalDateTime()
                            .toFormattedString(includeTime = true),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun FrameViewerCardMedia(mediaFile: DocumentFile?, modifier: Modifier) {
    var showDetails by viewModelApp.mediaViewDetails
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
fun FrameViewerCardMediaImage(mediaFile: DocumentFile?) {
    var showDetails by viewModelApp.mediaViewDetails
    val mediaViewRotates = viewModelApp.mediaViewRotates

    if (mediaFile == null) return

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
}

@Composable
fun FrameViewerCardTools(mediaFile: DocumentFile?) {
    var showDetails by viewModelApp.mediaViewDetails
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current

    if (mediaFile == null) return

    if (showDetails and !isLandscape) {
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

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
