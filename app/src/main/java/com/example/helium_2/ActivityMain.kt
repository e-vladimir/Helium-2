package com.example.helium_2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

import com.example.helium_2.ui.theme.Helium2Theme

import kotlinx.coroutines.flow.collectLatest


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class ActivityMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Helium2Theme { FrameApp() }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameApp() {
    val context = LocalContext.current
    val folders = viewModelApp.folders

    var currentFolder by viewModelApp.currentFolder
    var currentPage by viewModelApp.currentPage
    var loadingFolders by remember { mutableStateOf(true) }
    var menuExpanded by remember { mutableStateOf(false) }
    var savingFolder by remember { mutableStateOf(false) }
    var forgetFolder by remember { mutableStateOf(false) }

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

                folders.add(it.toString())

                savingFolder = true
            } catch (_: Exception) {
            }
        }
    }

    val pagerState =
        rememberPagerState(initialPage = currentPage) { if (currentFolder.isNotEmpty()) 2 else 1 }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            if (page != currentPage) {
                currentPage = page
            }
        }
    }

    LaunchedEffect(currentPage) {
        if (pagerState.currentPage != currentPage) {
            pagerState.animateScrollToPage(currentPage)
        }
    }

    LaunchedEffect(loadingFolders) {
        if (loadingFolders) {
            viewModelApp.loadFolders(context)
            loadingFolders = false
        }
    }

    LaunchedEffect(savingFolder) {
        if (savingFolder) {
            viewModelApp.saveFolders(context)
            savingFolder = false
        }
    }

    if (forgetFolder) {
        AlertDialog(
            onDismissRequest = {
                forgetFolder = false
            },
            title = { Text(text = "Забыть каталог?") },
            text = { Text(text = "Забыть $currentFolder?") },
            confirmButton = {
                TextButton(onClick = {
                    forgetFolder = false
                    viewModelApp.forgotCurrentFolder()
                    savingFolder = true
                }) { Text("Забыть") }
            },
            dismissButton = {
                TextButton(onClick = { forgetFolder = false }) { Text("Отмена") }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentPage) {
                            0 -> "Каталоги"
                            1 -> currentFolder
                            else -> "..."
                        }
                    )

                },
                actions = {
                    when (currentPage) {
                        0 -> {
                            IconButton(onClick = {
                                directoryPickerLauncher.launch(null)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                            }
                        }

                        1 -> {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                menuExpanded = false
                                forgetFolder = true
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) },
                            text = { Text("Забыть каталог") }
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                NavigationBarItem(
                    selected = currentPage == 0,
                    label = { Text(text = "Каталоги") },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Folder, contentDescription = "Каталоги"
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
                                imageVector = Icons.Outlined.Photo, contentDescription = "Лента"
                            )
                        },
                        onClick = { currentPage = 1 }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                0 -> FrameFolders(modifier = Modifier.padding(innerPadding))
                1 -> FrameMedia(modifier = Modifier.padding(innerPadding))
            }
        }

    }
}


@Preview(showSystemUi = true)
@Composable
fun AppPreview() {
    Helium2Theme { FrameApp() }
}
