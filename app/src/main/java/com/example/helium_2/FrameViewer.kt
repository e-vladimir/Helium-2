package com.example.helium_2

import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import coil.compose.AsyncImage


@Preview(showSystemUi = true)
@Composable
fun FrameViewer() {
    val folderCurrent by viewModelApp.folderCurrent
    val mediaFile by viewModelApp.mediaFile
    var showDetails by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
//        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(4.dp)
        ) {
            if (showDetails) {
                Card(
                    modifier = Modifier.padding(4.dp)
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
                            text = mediaFile?.lastModified()?.toLocalDateTime()
                                ?.toFormattedString(includeTime = true)!!,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showDetails = !showDetails },
                    model = mediaFile?.uri,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}