package com.example.helium_2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
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

import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile

import coil.compose.AsyncImage


@Preview(showSystemUi = true)
@Composable
fun FrameViewerCard() {
    val mediaFiles = viewModelApp.mediaFiles
    val pagerState = rememberPagerState(pageCount = { mediaFiles.count() })

    val mediaIdxs = mediaFiles.keys.toList().sortedDescending()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(state = pagerState) { page ->
            val mediaFile = mediaFiles[mediaIdxs[page]]

            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                FrameViewerCardInfo(mediaFile = mediaFile!!)
                FrameViewerCardMedia(mediaFile = mediaFile!!)
            }
        }
    }
}

@Composable
fun FrameViewerCardMedia(mediaFile: DocumentFile) {
    var showDetails by viewModelApp.mediaViewDetails

    if (showDetails) {
        ElevatedCard(
            modifier = Modifier
                .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxSize()
        ) {
            FrameViewerCardMediaImage(mediaFile = mediaFile)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            FrameViewerCardMediaImage(mediaFile = mediaFile)
        }
    }
}

@Composable
fun FrameViewerCardMediaImage(mediaFile: DocumentFile) {
    var showDetails by viewModelApp.mediaViewDetails

    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .clickable { showDetails = !showDetails }
            .background(color = Color.Black),
        model = mediaFile.uri,
        contentDescription = null,
        contentScale = ContentScale.Fit,
    )
}


@Composable
fun FrameViewerCardInfo(mediaFile: DocumentFile) {
    val folderCurrent by viewModelApp.folderCurrent
    var showDetails by viewModelApp.mediaViewDetails

    if (showDetails) {
        Card(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
        ) {
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