package com.example.helium_2

import android.content.Context
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    val context         = LocalContext.current

    var currentFolder  by viewModelApp.currentFolder
    var currentPage    by viewModelApp.currentPage
    var loadingFolders by viewModelApp.loadingFolders
    var savingFolder   by viewModelApp.savingFolder

    val pagerState      = rememberPagerState(initialPage = currentPage) { if (currentFolder.isNotEmpty()) 2 else 1 }

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

    Scaffold(
        modifier  =   Modifier.fillMaxSize(),
        topBar    = { TopBarApp() },
        bottomBar = { BottomBarApp() }
    ) { innerPadding ->
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state    = pagerState,
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
