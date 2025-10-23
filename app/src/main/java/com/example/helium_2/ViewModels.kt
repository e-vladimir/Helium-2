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
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlin.collections.forEach


val KEY_FOLDERS = stringPreferencesKey("folders")


enum class STATES {
    PROCESSING, WAITING
}


class ViewModelApp : ViewModel() {
    var dialogForgetFolderVisible = mutableStateOf(false)

    var folderCounters = mutableStateMapOf<String, String>()
    var folderCurrent = mutableStateOf("")
    var folderPaths = mutableListOf<Uri>()
    var folderProcessor = mutableStateOf<FolderProcessor?>(null)
    var folderProcessors = mutableMapOf<String, FolderProcessor>()

    var leftPanelVisible = mutableStateOf(false)

    var mediaState = mutableStateOf(STATES.WAITING)

    var menuFolderVisible = mutableStateOf(false)

    var dataDebug = mutableStateOf("")

    suspend fun saveFolderPaths(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FOLDERS] = folderPaths.joinToString("\n") { it.toString() }
        }
    }

    suspend fun loadFolderPaths(context: Context) {
        folderPaths = context
            .dataStore
            .data
            .map { preferences -> preferences[KEY_FOLDERS] ?: "" }
            .first()
            .split("\n")
            .filter { it.isNotEmpty() }
            .map { it.toUri() }
            .toMutableList()

        folderPaths.map { applyFolderPath(it) }
    }

    fun addFolderPath(folderPath: Uri) {
        if (folderPath in folderPaths) return
        if (uriToString(folderPath).isEmpty()) return

        folderPaths.add(folderPath)

        applyFolderPath(folderPath)
    }

    fun applyFolderPath(folderPath: Uri) {
        val folderName = uriToString(folderPath)

        if (folderName.isEmpty()) return

        folderCounters[folderName] = "waiting"
        folderProcessors[folderName] = FolderProcessor(folderPath)
    }

    suspend fun readFolderCounters(context: Context) {
        withContext(Dispatchers.IO) {
            folderCounters.forEach { (folderName, _) ->
                folderProcessors[folderName]?.readFiles(context)
                folderCounters[folderName] = folderProcessors[folderName]?.countFiles().toString()
            }
        }
    }

    fun switchFolderCurrentByName(folderName: String) {
        if (folderName == folderCurrent.value) return

        leftPanelVisible.value = false

        folderCurrent.value = folderName
        folderProcessor.value = folderProcessors[folderName]

        dataDebug.value = folderProcessor.value?.dataDebug ?: ""

    }

    fun forgetFolderCurrent(context: Context) {
        folderPaths =
            folderPaths.filterNot { folderPath -> uriToString(folderPath) == folderCurrent.value }
                .toMutableList()

        viewModelScope.launch {
            saveFolderPaths(context)

            folderCounters.remove(folderCurrent.value)
            folderProcessors.remove(folderCurrent.value)

            folderCurrent.value = ""
        }
    }
}