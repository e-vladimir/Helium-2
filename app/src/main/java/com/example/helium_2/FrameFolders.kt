// КАРКАС КАТАЛОГОВ

package com.example.helium_2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.helium_2.ui.theme.Helium2Theme


val folders = listOf("Камера", "Загрузки", "Документы", "Технопорно", "Просто.... порно")


@Composable
fun FrameFolders(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp, top = 10.dp)
    ) {
        folders.forEach { folder -> FrameFoldersItem(folder) }
        Spacer(modifier = Modifier.weight(1f))
        FrameFoldersItem("Корзина")
    }
}


@Composable
fun FrameFoldersItem(folderName: String = "") {
    var currentFolder by viewModelApp.currentFolder
    var currentPage by viewModelApp.currentPage

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isCurrentFolder = folderName == currentFolder

        Button(
            onClick = {
                if (!isCurrentFolder) {
                    currentFolder = folderName
                    currentPage = 1
                }
            },
            modifier = Modifier.fillMaxWidth(),
            border = if (isCurrentFolder) null else BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.primaryContainer
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCurrentFolder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                contentColor = if (isCurrentFolder) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isCurrentFolder) Icons.Default.Folder else Icons.Outlined.Folder,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = folderName)
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = (1..2000).random().toString(),
                    color = if (isCurrentFolder) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                    fontSize = 11.sp
                )
            }
        }

    }
}


@Preview
@Composable
fun FoldersPreview() {
    Helium2Theme { FrameFolders() }
}
