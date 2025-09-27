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

val KEY_FOLDERS = stringPreferencesKey("key_folders")

class ViewModelApp() : ViewModel() {

    var folders = mutableStateListOf<String>()
    var mediaDTimes = mutableStateListOf<LocalDate>()
    var currentPage = mutableIntStateOf(if (folders.isNotEmpty()) 1 else 0)
    var currentFolder = mutableStateOf("")

    fun foldersNames(): List<String> {
        return folders.map { folder -> folder.toString().replace("%3A", "/").replace("%2F", "/").split("/").last() }
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
                if (data.isEmpty()) emptyList()
                else data.split("\n")
            }.first()
        )
    }
}


val viewModelApp = ViewModelApp()
