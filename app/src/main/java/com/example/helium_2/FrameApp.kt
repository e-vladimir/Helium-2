// UI КАРКАС ПРИЛОЖЕНИЯ

package com.example.helium_2

import android.content.Intent
import android.net.Uri
import android.widget.Toast

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.HideImage

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController

import kotlinx.coroutines.launch


const val VERSION = "30 окт 2025"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameApp(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val leftPanelState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val stateRefresh = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    var leftPanelVisible by viewModelApp.leftPanelVisible
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            viewModelApp.updateCountFolderCurrent(context)
        }
        isRefreshing = false
    }

    LaunchedEffect(leftPanelVisible) {
        if (leftPanelState.isClosed and leftPanelVisible) leftPanelState.open()
        else if (leftPanelState.isOpen and !leftPanelVisible) leftPanelState.close()
    }

    LaunchedEffect(leftPanelState.currentValue) {
        leftPanelVisible = leftPanelState.isOpen
    }

    ModalNavigationDrawer(
        drawerContent = { FrameFolders() }, drawerState = leftPanelState
    ) {
        Scaffold(
            modifier =
                Modifier.pullToRefresh(
                    state = stateRefresh,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                ),
            topBar = { FrameAppTopBar() }) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                FrameMedia(navController = navController)
                FrameAppIndicatorPullToRefresh(
                    Modifier.align(Alignment.TopCenter),
                    stateRefresh,
                    isRefreshing
                )
                FrameAppIndicatorRefresh(Modifier.align(Alignment.Center))

            }
        }
    }
}


@Composable
fun FrameAppIndicatorPullToRefresh(
    modifier: Modifier = Modifier,
    state: PullToRefreshState,
    flag: Boolean
) {
    PullToRefreshDefaults.Indicator(
        modifier = modifier,
        state = state,
        isRefreshing = flag
    )
}


@Composable
fun FrameAppIndicatorRefresh(modifier: Modifier = Modifier) {
    val mediaState by viewModelApp.mediaState

    if (mediaState == STATES.PROCESSING) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
        )

        Box(
            modifier = modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center

        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp),
                strokeWidth = 2.dp
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameAppTopBar() {
    val folderSelected by viewModelApp.folderCurrent

    TopAppBar(
        title = { Text(folderSelected.ifEmpty { "Helium-2" }) },
        navigationIcon = { FrameAppHeaderNavigationIcon() },
        actions = { FrameAppHeaderActions() })
}


@Composable
fun FrameAppHeaderNavigationIcon() {
    var leftPanelVisible by viewModelApp.leftPanelVisible

    IconButton(onClick = { leftPanelVisible = true }) {
        Icon(Icons.Default.Menu, contentDescription = "Menu")
    }
}


@Composable
fun FrameAppHeaderActions() {
    val folderSelected by viewModelApp.folderCurrent
    var menuFolderVisible by viewModelApp.menuFolderVisible

    if (folderSelected.isEmpty()) return

    IconButton(onClick = { menuFolderVisible = true }) {
        Icon(Icons.Default.MoreVert, "Меню")
    }

    MenuFolder()
}


@Composable
fun MenuFolder() {
    val folderSelected by viewModelApp.folderCurrent
    val context = LocalContext.current
    var dialogForgetFolderVisible by viewModelApp.dialogForgetFolderVisible
    var menuFolderVisible by viewModelApp.menuFolderVisible

    if (dialogForgetFolderVisible) {
        AlertDialog(
            onDismissRequest = { dialogForgetFolderVisible = false },
            title = { Text("Забыть каталог?") },
            text = { Text("Забыть каталог $folderSelected?") },
            confirmButton = {
                Button(
                    onClick = {
                        dialogForgetFolderVisible = false
                        viewModelApp.forgetFolderCurrent(context)
                    }) {
                    Text("Забыть")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { dialogForgetFolderVisible = false }) {
                    Text("Оставить")
                }
            })
    }


    DropdownMenu(
        expanded = menuFolderVisible, onDismissRequest = { menuFolderVisible = false }) {
        DropdownMenuItem(onClick = {
            menuFolderVisible = false
            dialogForgetFolderVisible = true
        }, text = { Text("Забыть каталог") }, leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.HideImage, contentDescription = null
            )
        })
    }
}


@Composable
fun FrameFolders() {
    val foldersCounters = viewModelApp.folderCounters

    ModalDrawerSheet(
        modifier = Modifier.statusBarsPadding()
    ) {
        FoldersHeader()

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            foldersCounters.toSortedMap().forEach { (folder, count) -> ButtonFolder(folder, count) }

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
            Text(
                text = "Helium-2", fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Версия от $VERSION",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

    }
}


@Composable
fun ButtonAddFolder() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                coroutineScope.launch {
                    viewModelApp.addFolderPath(it)
                    viewModelApp.saveFolderPaths(context)
                    viewModelApp.readFolderCounters(context)
                }

            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    NavigationDrawerItem(label = {
        Text(
            text = "Добавить каталог", color = MaterialTheme.colorScheme.primary
        )
    }, selected = false, icon = {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }, badge = { }, onClick = { directoryPickerLauncher.launch(null) })
}


@Composable
fun ButtonFolder(folder: String, count: String) {
    val context = LocalContext.current
    var currentFolder by viewModelApp.folderCurrent
    val coroutineScope = rememberCoroutineScope()
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
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp), strokeWidth = 2.dp
            )
        }
    }, onClick = {
        coroutineScope.launch {
            viewModelApp.switchFolderCurrentByName(folder, context)
        }
    })
}
