// КАРКАС КАТАЛОГОВ

package com.example.helium_2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun FrameFolders(modifier: Modifier = Modifier) {
    val folders = listOf("Камера", "Загрузки", "Документы", "Технопорно")

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        items(folders) { folder -> FrameFoldersItem(folder) }

        item { FrameFoldersButton() }
    }
}


@Composable
fun FrameFoldersItem(folder_name: String = "") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            modifier = Modifier
                .offset(8.dp, 8.dp)
                .align(Alignment.TopStart),
            text = folder_name.uppercase(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier
                .offset(-8.dp, -8.dp)
                .align(Alignment.BottomEnd),
            text = (1..1209).random().toString(),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
fun FrameFoldersButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        IconButton(
            onClick = {}, modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                modifier = Modifier.size(50.dp, 50.dp),
                imageVector = Icons.Filled.Add,
                contentDescription = "Добавить каталог",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}