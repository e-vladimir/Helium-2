// VIEW-МОДЕЛИ

package com.example.helium_2

import android.content.Context
import android.content.Intent

import android.net.Uri

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf

import androidx.core.net.toUri

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.documentfile.provider.DocumentFile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.time.LocalDate
import java.time.LocalDateTime

import kotlin.collections.set


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
    var mediaFiles = mutableStateMapOf<LocalDateTime, DocumentFile>()
    var mediaGroups = mutableStateMapOf<LocalDate, Map<LocalDateTime, DocumentFile>>()
    var mediaFile = mutableStateOf<DocumentFile?>(null)

    var menuFolderVisible = mutableStateOf(false)

    var mediaViewDetails = mutableStateOf(false)
    var mediaViewRotates = mutableStateMapOf<DocumentFile, Float>()


    suspend fun saveFolderPaths(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FOLDERS] = folderPaths.joinToString("\n") { it.toString() }
        }
    }

    suspend fun loadFolderPaths(context: Context) {
        folderPaths =
            context.dataStore.data.map { preferences -> preferences[KEY_FOLDERS] ?: "" }.first()
                .split("\n").filter { it.isNotEmpty() }.map { it.toUri() }.toMutableList()

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

    suspend fun updateCountFolderCurrent(context: Context) {
        if (folderCurrent.value.isEmpty()) return
        mediaState.value = STATES.PROCESSING

        withContext(Dispatchers.IO) {
            folderCounters[folderCurrent.value] =
                (folderProcessors[folderCurrent.value]?.readFiles(context) ?: 0).toString()
            updateMediaGroups()
        }

        mediaState.value = STATES.WAITING
    }

    suspend fun readFolderCounters(context: Context) {
        withContext(Dispatchers.IO) {
            folderCounters.keys.map { folderName ->
                async {
                    folderCounters[folderName] =
                        (folderProcessors[folderName]?.readFiles(context) ?: 0).toString()
                }
            }.awaitAll()
        }
    }

    suspend fun switchFolderCurrentByName(folderName: String, context: Context) {
        if (folderName == folderCurrent.value) return

        leftPanelVisible.value = false

        folderCurrent.value = folderName
        folderProcessor.value = folderProcessors[folderName]

        mediaState.value = STATES.PROCESSING
        mediaFiles.clear()

        updateMediaGroups()
    }

    suspend fun updateMediaGroups() {
        withContext(Dispatchers.IO) {
            mediaFiles.putAll(folderProcessor.value?.files?.map {
                it.lastModified().toLocalDateTime() to it
            }!!.toMap())
        }

        viewModelScope.launch {
            mediaGroups.clear()
            mediaGroups.putAll(mediaFiles.keys.map { it.toLocalDate() }
                .map { mediaGroup -> mediaGroup to mediaFiles.filter { it.key.toLocalDate() == mediaGroup } })
        }

        mediaState.value = STATES.WAITING
    }

    fun rotateMediaToCw(mediaFile: DocumentFile) {
        mediaViewRotates[mediaFile] = (mediaViewRotates[mediaFile] ?: 0.0f) + 90.0f
    }
}