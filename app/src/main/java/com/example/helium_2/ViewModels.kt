// VIEW-МОДЕЛИ

package com.example.helium_2

import android.content.Context

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf

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

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import java.util.Locale

import kotlin.collections.forEach


val KEY_FOLDERS = stringPreferencesKey("folders")


enum class STATES {
    PROCESSING, WAITING
}


fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this), ZoneId.systemDefault()
    )
}


fun LocalDateTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return this.format(formatter).toString()
}


fun LocalDate.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return this.format(formatter).toString()
}


class ViewModelApp : ViewModel() {
    var appHeader = mutableStateOf("Helium-2")
    var folderCounters = mutableStateMapOf<String, String>()
    var folderPaths = mutableListOf<Uri>()
    var folderProcessors = mutableMapOf<String, FolderProcessor>()
    var leftPanelVisible = mutableStateOf(false)
    var menuFolderVisible = mutableStateOf(false)
    var dialogForgetFolderVisible = mutableStateOf(false)
    var debugData = mutableStateOf("")
    var folderProcessor = mutableStateOf<FolderProcessor?>(null)
    var mediaDates = mutableStateListOf<String>()
    var mediaState = mutableStateOf(STATES.WAITING)

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

    suspend fun updateCounts(context: Context) {
        withContext(Dispatchers.IO) {
            folderCounters.forEach { (folderName, _) -> updateCount(folderName, context) }
        }
    }

    fun switchFolder(folderName: String) {
        if (folderName == appHeader.value) return

        leftPanelVisible.value = false

        appHeader.value = folderName
        folderProcessor.value = folderProcessors[folderName]

        mediaDates.clear()

        viewModelScope.launch { collectMediaDates() }
    }

    fun forgetFolder(context: Context) {
        debugData.value = ""

        folderPaths =
            folderPaths.filterNot { folderPath -> uriToString(folderPath) == appHeader.value }
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

    suspend fun collectMediaDates() {
        mediaState.value = STATES.PROCESSING

        val dates = withContext(Dispatchers.IO) {
            folderProcessor.value?.files
                ?.map { it.lastModified().toLocalDateTime().toLocalDate() }
                ?.toSortedSet()
                ?.map { it.toFormattedString() }
                ?: emptyList()
        }

        mediaDates.clear()
        mediaDates.addAll(dates)
        mediaState.value = STATES.WAITING
    }
}