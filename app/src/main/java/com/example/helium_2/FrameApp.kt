// UI КАРКАС ПРИЛОЖЕНИЯ

package com.example.helium_2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Folder

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameApp() {
    val leftPanelState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val frameScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            FrameFolders()
        },
        drawerState = leftPanelState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Helium-2") },
                    navigationIcon = {
                        IconButton(onClick = {
                            frameScope.launch {
                                if (leftPanelState.isClosed) leftPanelState.open()
                                else leftPanelState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            FrameMedia(modifier = Modifier.padding(innerPadding))
        }
    }
}


@Composable
fun FrameFolders() {
    val foldersCounters = viewModelApp.foldersCounters

    ModalDrawerSheet(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ButtonAddFolder()

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            foldersCounters.forEach { (folder, count) -> ButtonFolder(folder, count) }
        }
    }
}


@Composable
fun ButtonAddFolder() {
    NavigationDrawerItem(
        label = { Text("КАТАЛОГИ", fontWeight = FontWeight.Bold) },
        selected = false,
        icon = { Icon(Icons.Filled.FolderCopy, contentDescription = null) },
        badge = {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = 12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,

                )
            }
        },
        onClick = { }
    )
}


@Composable
fun ButtonFolder(folder: String, count: String) {
    var currentFolder by viewModelApp.currentFolder
    val selected = currentFolder == folder

    NavigationDrawerItem(
        label = { Text(folder) },
        selected = selected,
        icon = {
            Icon(
                imageVector = if (selected) Icons.Filled.Folder else Icons.Outlined.Folder,
                contentDescription = null
            )
        },
        badge = {
            Text(
                text = count,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.outline
            )
        },
        onClick = { currentFolder = folder }
    )
}
