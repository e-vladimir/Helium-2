// ОБЪЕКТЫ

package com.example.helium_2

import android.content.Context
import android.net.Uri

import androidx.documentfile.provider.DocumentFile


class FolderProcessor(val folderPath: Uri) {
    val files = mutableListOf<MediaFile>()

    fun readFiles(context: Context): Int? {
        files.clear()
        try {
            files.addAll(
                DocumentFile
                .fromTreeUri(context, folderPath)
                ?.listFiles()!!
                .map { MediaFile(it) }
                .filter { it.isImage }
                .toList())

            return countFiles()
        } catch (_: Exception) {
        }

        return null
    }

    fun countFiles(): Int = files.count()

}
