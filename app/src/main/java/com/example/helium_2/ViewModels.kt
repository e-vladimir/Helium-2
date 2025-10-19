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

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

import kotlin.collections.forEach


val KEY_FOLDERS = stringPreferencesKey("folders")


class ViewModelApp : ViewModel() {
    var appHeader = mutableStateOf("Helium-2")
    var folderCounters = mutableStateMapOf<String, String>()
    var folderPaths = mutableListOf<Uri>()
    var folderProcessors = mutableMapOf<String, FolderProcessor>()
    var leftPanelVisible = mutableStateOf(false)
    var menuFolderVisible = mutableStateOf(false)
    var dialogForgetFolderVisible = mutableStateOf(false)
    var debugData = mutableStateOf("")

    fun appendFolderPath(folderPath: Uri) {
        if (folderPath in folderPaths) return
        if (uriToString(folderPath).isEmpty()) return

        folderPaths.add(folderPath)

        applyFolderPath(folderPath)
    }

    suspend fun saveFolderPaths(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FOLDERS] = folderPaths.joinToString("\n") { it.toString() }
        }
    }

    suspend fun loadFolderPaths(context: Context) {
        val data =
            context.dataStore.data.map { preferences -> preferences[KEY_FOLDERS] ?: "" }.first()
                .split("\n").filter { it.isNotEmpty() }.map { it.toUri() }

        folderPaths = data.toMutableList()
        folderPaths.map { applyFolderPath(it) }
    }

    fun applyFolderPath(folderPath: Uri) {
        var folderName = uriToString(folderPath)

        if (folderName.isEmpty()) folderName = "Память телефона"

        folderCounters[folderName] = "waiting"
        folderProcessors[folderName] = FolderProcessor(folderPath)
    }

    fun updateCount(folderName: String, context: Context) {
        folderProcessors[folderName]?.readFiles(context)
        folderCounters[folderName] = folderProcessors[folderName]?.countFiles().toString()
    }

    fun updateCounts(context: Context) {
        folderCounters.forEach { (folderName, _) -> updateCount(folderName, context) }
    }

    fun switchFolder(folderName: String) {
        if (folderName == appHeader.value) return

        leftPanelVisible.value = false
        appHeader.value = folderName
    }

    fun forgetFolder(context: Context) {
        debugData.value = ""

        folderPaths = folderPaths
            .filterNot { folderPath -> uriToString(folderPath) == appHeader.value }
            .toMutableList()

        folderPaths.forEach { folderPath ->
            val folderName = uriToString(folderPath)

            debugData.value += "$folderName ::\n"
            debugData.value += folderPath.toString()
            debugData.value += "\n\n"

        }

        viewModelScope.launch {
            saveFolderPaths(context)

            folderCounters.remove(appHeader.value)
            folderProcessors.remove(appHeader.value)

            appHeader.value = "Helium-2"
        }
    }
}
