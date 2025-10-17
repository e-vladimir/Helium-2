// VIEW-МОДЕЛИ

package com.example.helium_2

import android.content.Context

import android.net.Uri

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf

import androidx.core.net.toUri

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.collections.forEach


val KEY_FOLDERS = stringPreferencesKey("folders")


class ViewModelApp : ViewModel() {
    var currentFolder = mutableStateOf("")
    var folderCounters = mutableStateMapOf<String, String>()
    var folderPaths = mutableListOf<Uri>()
    var folderProcessors = mutableMapOf<String, FolderProcessor>()

    suspend fun appendFolderPath(folderPath: Uri) {
        if (folderPath in folderPaths) return

        folderPaths.add(folderPath)

        applyFolderPath(folderPath)
    }

    suspend fun saveFoldersPaths(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FOLDERS] = folderPaths.joinToString("\n") { it.toString() }
        }
    }

    suspend fun loadFoldersPaths(context: Context) {
        val data =
            context.dataStore.data.map { preferences -> preferences[KEY_FOLDERS] ?: "" }.first()
                .split("\n").filter { it.isNotEmpty() }.map { it.toUri() }

        folderPaths = data.toMutableList()
        folderPaths.map { applyFolderPath(it) }
    }

    suspend fun applyFolderPath(folderPath: Uri) {
        val folderName =
            folderPath.toString().replace("%20", " ").replace("%3A", ":").replace("%2F", "/")
                .split(":").last().split("/").last()

        folderCounters[folderName] = "waiting"
        folderProcessors[folderName] = FolderProcessor(folderPath)
    }

    suspend fun updateCount(folderName: String, context: Context) {
        folderProcessors[folderName]?.readFiles(context)
        folderCounters[folderName] = folderProcessors[folderName]?.countFiles().toString()
    }

    suspend fun updateCounts(context: Context) {
        folderCounters.forEach { (folderName, _) -> updateCount(folderName, context) }
    }
}
