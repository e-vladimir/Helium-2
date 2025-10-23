// ОБЪЕКТЫ

package com.example.helium_2

import android.content.Context

import android.net.Uri

import androidx.documentfile.provider.DocumentFile


class FolderProcessor(val folderPath: Uri) {
    var files = listOf<DocumentFile>()
    var dataDebug = ""

    fun readFiles(context: Context) {
        val whiteMime = listOf("image/", "video/")

        try {
            files = DocumentFile
                    .fromTreeUri(context, folderPath)
                    ?.listFiles()!!.filter { documentFile -> whiteMime.any{mime -> documentFile.type?.startsWith(mime) == true } } .toList()

            dataDebug = files.withIndex().joinToString("\n"){(idx, text) -> "[$idx] ${text.name}"}
        } catch (e: Exception) {
            dataDebug = e.toString()
        }
    }

    fun countFiles(): Int = files.count()
}
