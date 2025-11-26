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
    val appStarted = mutableStateOf(false)

    val dialogForgetFolderVisible = mutableStateOf(false)

    val folderCounters = mutableStateMapOf<String, String>()
    val folderCurrent = mutableStateOf("")
    val folderPaths = mutableListOf<Uri>()
    val folderProcessor = mutableStateOf<FolderProcessor?>(null)
    val folderProcessors = mutableMapOf<String, FolderProcessor>()

    val leftPanelVisible = mutableStateOf(false)

    val mediaState = mutableStateOf(STATES.WAITING)
    val mediaFiles = mutableStateMapOf<LocalDateTime, MediaFile>()
    val mediaGroups = mutableStateMapOf<LocalDate, Map<LocalDateTime, MediaFile>>()
    val mediaFile = mutableStateOf<MediaFile?>(null)

    val menuFolderVisible = mutableStateOf(false)

    val mediaViewDetails = mutableStateOf(false)
    val mediaViewRotates = mutableStateMapOf<MediaFile, Float>()
    val mediaViewHiddens = mutableStateMapOf<MediaFile, Boolean>()
    val dialogDeleteMediaFileVisible = mutableStateOf(false)


    suspend fun saveFolderPaths(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FOLDERS] = folderPaths.joinToString("\n") { it.toString() }
        }
    }

    suspend fun loadFolderPaths(context: Context) {
        folderPaths.clear()
        folderPaths.addAll(context.dataStore.data.map { preferences -> preferences[KEY_FOLDERS] ?: "" }.first().split("\n").filter { it.isNotEmpty() }
            .map { it.toUri() }.toMutableList())

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
        try {
            folderPaths.remove(folderCurrent.value.toUri())

        } finally {
            viewModelScope.launch {
                saveFolderPaths(context)

                folderCounters.remove(folderCurrent.value)
                folderProcessors.remove(folderCurrent.value)

                folderCurrent.value = ""
            }
        }
    }

    suspend fun updateCountFolderCurrent(context: Context) {
        if (folderCurrent.value.isEmpty()) return
        mediaState.value = STATES.PROCESSING

        withContext(Dispatchers.IO) {
            folderCounters[folderCurrent.value] = (folderProcessors[folderCurrent.value]?.readFiles(context) ?: 0).toString()
            updateMediaGroups()
        }

        mediaState.value = STATES.WAITING
    }

    suspend fun readFolderCounters(context: Context) {
        withContext(Dispatchers.IO) {
            folderCounters.keys.map { folderName ->
                async {
                    folderCounters[folderName] = (folderProcessors[folderName]?.readFiles(context) ?: 0).toString()
                }
            }.awaitAll()
        }

        appStarted.value = true
    }

    fun switchFolderCurrentByName(folderName: String) {
        if (folderName == folderCurrent.value) return

        leftPanelVisible.value = false

        folderCurrent.value = folderName
        folderProcessor.value = folderProcessors[folderName]

        mediaState.value = STATES.PROCESSING

        updateMediaGroups()
    }

    fun updateMediaGroups() {
        mediaFiles.clear()
        mediaFiles.putAll(folderProcessor.value?.files?.map {
            it.fileTime to it
        }!!.toMap())

        mediaGroups.clear()
        mediaGroups.putAll(mediaFiles.keys.map { it.toLocalDate() }
            .map { mediaGroup -> mediaGroup to mediaFiles.filter { it.key.toLocalDate() == mediaGroup } })

        mediaState.value = STATES.WAITING
    }

    fun rotateMediaToCw(mediaFile: MediaFile) {
        mediaViewRotates[mediaFile] = (mediaViewRotates[mediaFile] ?: 0.0f) + 90.0f
    }

    fun switchVisibleMediaFile(mediaFile: MediaFile) {
        mediaViewHiddens[mediaFile] = mediaFile.switchVisible()
    }

    fun deleteMediaFile(mediaFile: MediaFile?): Boolean {
        if (mediaFile == null) return false
        if (!mediaFile.delete()) return false

        folderProcessor.value?.forgetFile(mediaFile)
        updateMediaGroups()

        return true
    }
}