// VIEW-МОДЕЛИ

package com.example.helium_2

import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel


class ViewModelApp : ViewModel() {
    var foldersCounters = mutableStateMapOf<String, String>()
    var currentFolder = mutableStateOf("")

    fun appendFolder(folderPath: Uri) {
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
