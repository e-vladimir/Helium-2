// КАРКАС МЕДИА

package com.example.helium_2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import java.time.LocalDate
import java.time.format.DateTimeFormatter


val dTimes = listOf(
    LocalDate.of(2025, 1, 1),
    LocalDate.of(2025, 2, 3),
    LocalDate.of(2025, 3, 5),
    LocalDate.of(2025, 4, 10),
    LocalDate.of(2025, 5, 23),
    LocalDate.of(2025, 6, 30),
    LocalDate.of(2025, 7, 2),
    LocalDate.of(2025, 8, 12),
    LocalDate.of(2025, 9, 21),
    LocalDate.of(2025, 10, 31),
    LocalDate.of(2025, 11, 11),
    LocalDate.of(2025, 12, 1),
)


@Preview
@Composable
fun FrameMedia(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
    ) {
        dTimes.forEach { dTime ->
            item(span = { GridItemSpan(4) }) {
                val groupName =
                    dTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).replace(".", "")
                FrameMediaGroup(groupName)
            }

            val files = List((1..10).random()) { "" }

            items(files) { file -> FrameMediaItem() }
        }
    }
}


@Composable
fun FrameMediaGroup(name: String = "") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 2.dp)
    ) {
        Text(text = name)
    }
}

@Composable
fun FrameMediaItem() {
    Box(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .background(Color.LightGray)
    )
    {}
}