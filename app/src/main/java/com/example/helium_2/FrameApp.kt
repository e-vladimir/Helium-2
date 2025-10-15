// UI КАРКАС ПРИЛОЖЕНИЯ

package com.example.helium_2

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameApp() {
    val leftPanelState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val frameScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            FrameFolders()
        }, drawerState = leftPanelState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Helium-2") }, navigationIcon = {
                    IconButton(onClick = {
                        frameScope.launch {
                            if (leftPanelState.isClosed) leftPanelState.open()
                            else leftPanelState.close()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                })
            }) { innerPadding ->
            FrameMedia(modifier = Modifier.padding(innerPadding))
        }
    }
}


@Preview
@Composable
fun FrameFolders() {
    val foldersCounters = viewModelApp.foldersCounters

    ModalDrawerSheet(
        modifier = Modifier.statusBarsPadding()
    ) {
        FoldersHeader()

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            foldersCounters.forEach { (folder, count) -> ButtonFolder(folder, count) }

            ButtonAddFolder()
        }
    }
}


@Composable
fun FoldersHeader() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Helium-2", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Версия от 15 окт 2025",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

    }
}

@Composable
fun ButtonAddFolder() {
    val context = LocalContext.current

    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                viewModelApp.appendFolder(it)
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    NavigationDrawerItem(
        label = {
            Text(text = "Добавить каталог", color = MaterialTheme.colorScheme.primary)
        }, selected = false, icon = {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        badge = { }, onClick = {
            directoryPickerLauncher.launch(null)
        }
    )
}


@Composable
fun ButtonFolder(folder: String, count: String) {
    var currentFolder by viewModelApp.currentFolder
    val selected = currentFolder == folder

    NavigationDrawerItem(label = { Text(folder) }, selected = selected, icon = {
        Icon(
            imageVector = if (selected) Icons.Filled.Folder else Icons.Outlined.Folder,
            contentDescription = null
        )
    }, badge = {
        if (count.isDigitsOnly()) {
            Text(
                text = count, fontSize = 10.sp, color = MaterialTheme.colorScheme.outline
            )
        } else {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        }
    }, onClick = { currentFolder = folder })
}
