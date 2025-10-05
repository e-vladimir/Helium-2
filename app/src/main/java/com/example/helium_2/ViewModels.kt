// МОДЕЛИ

package com.example.helium_2

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

import java.time.LocalDate


class ViewModelApp() : ViewModel() {
    val KEY_FOLDERS = stringPreferencesKey("key_folders")

    var folders = mutableStateListOf<String>()
    var currentFolder = mutableStateOf("")

    //    var currentPage    = mutableIntStateOf(if (folders.isNotEmpty()) 1 else 0)
    var currentPage = mutableIntStateOf(0)
    var forgetFolder = mutableStateOf(false)
    var loadingFolders = mutableStateOf(true)
    var mediaDTimes = mutableStateListOf<LocalDate>()
    var menuExpanded = mutableStateOf(false)
    var savingFolder = mutableStateOf(false)

    fun foldersNames(): List<String> {
        return folders.map { folder ->
            folder.replace("%3A", "/").replace("%2F", "/").split("/").last()
        }.sorted()
    }

    suspend fun saveFolders(appContext: Context) {
        appContext.dataStore.edit { preferences ->
            preferences[KEY_FOLDERS] = folders.joinToString("\n")
        }
    }

    suspend fun loadFolders(appContext: Context) {
        folders.clear()
        folders.addAll(
            appContext.dataStore.data.map { preferences ->
                val data = preferences[KEY_FOLDERS] ?: ""

                if (data.isEmpty()) emptyList() else data.split("\n")
            }.first()
        )
    }

    fun forgotCurrentFolder() {
        folders.removeIf { it -> it.contains(currentFolder.value) }
        currentFolder.value = ""
    }
}


val viewModelApp = ViewModelApp()
