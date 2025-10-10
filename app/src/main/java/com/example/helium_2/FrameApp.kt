// UI КАРКАС ПРИЛОЖЕНИЯ

package com.example.helium_2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameApp() {
    val leftPanelState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val frameScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        drawerContent = {
            FrameFolders()
        },
        drawerState = leftPanelState
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
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
    val folders = viewModelApp.folders
    var currentFolder by viewModelApp.currentFolder

    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            folders.forEach { folder ->
                NavigationDrawerItem(
                    label = { Text(folder.name) },
                    selected = currentFolder == folder.name,
                    icon = { Icon(Icons.Outlined.Folder, contentDescription = null) },
                    badge = { Text(folder.count.toString()) },
                    onClick = { currentFolder = folder.name }
                )
            }
        }
    }
}
