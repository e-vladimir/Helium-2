// ПАНЕЛИ УПРАВЛЕНИЯ

package com.example.helium_2

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarApp() {
    var currentPage by viewModelApp.currentPage

    TopAppBar(
        title = { TopBarAppTitle() },
        actions = {
            when (currentPage) {
                0 -> TopBarAppButtonAppendFolder()
                1 -> {
                    MenuFolder()
                    TopBarAppButtonMenuFolder()
                }
            }
        }
    )
}


@Composable
fun TopBarAppTitle() {
    var currentFolder by viewModelApp.currentFolder
    var currentPage by viewModelApp.currentPage

    Text(
        text = when (currentPage) {
            0 -> "Каталоги"
            1 -> currentFolder
            else -> "..."
        }
    )
}


@Composable
fun TopBarAppButtonAppendFolder() {
    val context = LocalContext.current
    val folders = viewModelApp.folders
    var savingFolder by viewModelApp.savingFolder

    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                folders.add(it.toString())
                savingFolder = true
            } catch (_: Exception) {
            }
        }
    }

    IconButton(onClick = {
        directoryPickerLauncher.launch(null)
    }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }

}


@Composable
fun TopBarAppButtonMenuFolder() {
    var menuExpanded by viewModelApp.menuExpanded

    IconButton(onClick = { menuExpanded = true }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null
        )
    }

}


@Composable
fun BottomBarApp() {
    var currentFolder by viewModelApp.currentFolder
    var currentPage by viewModelApp.currentPage

    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        NavigationBarItem(
            selected = currentPage == 0,
            label = { Text(text = "Каталоги") },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = "Каталоги"
                )
            },
            onClick = { currentPage = 0 }
        )

        if (currentFolder.isNotEmpty()) {
            NavigationBarItem(
                selected = currentPage == 1,
                label = { Text(text = currentFolder) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Photo,
                        contentDescription = "Лента"
                    )
                },
                onClick = { currentPage = 1 }
            )
        }
    }
}