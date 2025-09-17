// КАРКАС КОРЗИНА

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Preview
@Composable
fun FrameBasket(modifier: Modifier = Modifier) {
    val dtimes = listOf(
        LocalDate.of(2021, 1, 1),
        LocalDate.of(2022, 2, 3),
        LocalDate.of(2023, 3, 5),
        LocalDate.of(2024, 4, 10),
        LocalDate.of(2025, 5, 23),
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        dtimes.forEach { dtime ->
            item(span = { GridItemSpan(4) }) {
                val group_name =
                    dtime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).replace(".", "")
                FrameBasketGroup(group_name)
            }

            val files = List((1..10).random()) { "" }

            items(files) { file -> FrameBasketItem() }
        }
    }
}


@Composable
fun FrameBasketGroup(name: String = "") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 2.dp)
            .background(color = MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun FrameBasketItem() {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    )
    {}
}