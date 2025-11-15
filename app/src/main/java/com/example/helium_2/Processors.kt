// ОБЪЕКТЫ

package com.example.helium_2

import android.content.Context
import android.net.Uri

import androidx.documentfile.provider.DocumentFile


class FolderProcessor(val folderPath: Uri) {
    var files = listOf<MediaFile>()

    fun readFiles(context: Context): Int? {
        try {
            files = DocumentFile
                .fromTreeUri(context, folderPath)
                ?.listFiles()!!
                .map { MediaFile(it) }
                .filter { it.isImage }
                .toList()

            return countFiles()
        } catch (_: Exception) {
        }

        return null
    }

    fun countFiles(): Int = files.count()

}
