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

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val KEY_FOLDERS = stringPreferencesKey("folders")


class ViewModelApp : ViewModel() {
    var currentFolder = mutableStateOf("")
    var foldersCounters = mutableStateMapOf<String, String>()
    var foldersPaths = mutableListOf<Uri>()

    suspend fun appendFolderPath(folderPath: Uri) {
        if (folderPath in foldersPaths) return

        foldersPaths.add(folderPath)

        applyFolderPath(folderPath)
    }

    suspend fun saveFoldersPaths(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FOLDERS] = foldersPaths.joinToString("\n") { it.toString() }
        }
    }

    suspend fun loadFoldersPaths(context: Context) {
        val data = context
            .dataStore
            .data
            .map { preferences -> preferences[KEY_FOLDERS] ?: "" }
            .first()
            .split("\n")
            .filter { it.isNotEmpty() }
            .map { it.toUri() }

        foldersPaths = data.toMutableList()
        foldersPaths.map { applyFolderPath(it) }
    }

    suspend fun applyFolderPath(folderPath: Uri) {
        val folderName =
            folderPath.toString()
                .replace("%20", " ")
                .replace("%3A", ":")
                .replace("%2F", "/")
                .split(":").last()
                .split("/").last()

        foldersCounters[folderName] = "waiting"
    }
}
