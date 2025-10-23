package com.example.helium_2

import android.content.Context

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview

import androidx.datastore.preferences.preferencesDataStore

import androidx.lifecycle.lifecycleScope

import com.example.helium_2.ui.theme.Helium2Theme

import kotlinx.coroutines.launch


val viewModelApp = ViewModelApp()
val Context.dataStore by preferencesDataStore(name = "settings")


class ActivityMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        lifecycleScope.launch {
            viewModelApp.loadFolderPaths(this@ActivityMain)
            viewModelApp.readFolderCounters(this@ActivityMain)
        }

        setContent {
            Helium2Theme { FrameApp() }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AppPreview() {
    Helium2Theme { FrameApp() }
}
