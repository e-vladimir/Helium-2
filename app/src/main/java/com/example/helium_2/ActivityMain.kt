package com.example.helium_2

import android.content.Context
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable

import androidx.datastore.preferences.preferencesDataStore

import androidx.lifecycle.lifecycleScope

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.helium_2.ui.theme.Helium2Theme

import kotlinx.coroutines.launch


enum class SCREENS(val screen: String) {
    FOLDER("folder"),
    MEDIA("media")
}


val Context.dataStore by preferencesDataStore(name = "settings")
val viewModelApp = ViewModelApp()


class ActivityMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        lifecycleScope.launch {
            viewModelApp.loadFolderPaths(this@ActivityMain)
            viewModelApp.readFolderCounters(this@ActivityMain)
        }

        setContent {
            Helium2Theme{ App() }
        }
    }
}


@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SCREENS.FOLDER.screen) {
        composable(SCREENS.FOLDER.screen) {
            FrameApp(navController = navController)
        }

        composable(SCREENS.MEDIA.screen) {
            FrameViewerCard(navController = navController)
        }
    }


}
