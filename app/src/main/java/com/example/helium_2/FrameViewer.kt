package com.example.helium_2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.navigation.NavController

import coil.compose.AsyncImage


@Composable
fun FrameViewer(navController: NavController) {
    val mediaFile by viewModelApp.mediaFile

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color= Color.Black.copy(alpha = 0.85f))
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = mediaFile?.uri,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

    }
}
