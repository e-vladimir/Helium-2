// ОБЪЕКТЫ

package com.example.helium_2

import android.content.Context

import android.net.Uri

import androidx.documentfile.provider.DocumentFile


class FolderProcessor(val folderPath: Uri) {
    var files = listOf<DocumentFile>()

    fun readFiles(context: Context) {
        try {
            files = DocumentFile
                .fromTreeUri(context, folderPath)
                ?.listFiles()
                ?.toList()!!
        } catch (_: Exception) {
        }
    }

    fun countFiles(): Int = files.count()
}
