// ОБЪЕКТЫ

package com.example.helium_2

import android.content.Context

import android.net.Uri

import androidx.documentfile.provider.DocumentFile

import java.time.LocalDate


class FolderProcessor(val folderPath: Uri) {
    var files = listOf<DocumentFile>()
    var dataDebug = ""

    fun readFiles(context: Context) {
        val whiteMime = listOf("image/", "video/")

        try {
            files = DocumentFile
                .fromTreeUri(context, folderPath)
                ?.listFiles()!!
                .filter { documentFile -> whiteMime.any { mime -> documentFile.type?.startsWith(mime) == true } }
                .toList()
        } catch (_: Exception) {
        }
    }

    fun countFiles(): Int = files.count()

    fun readDates(): List<LocalDate> {
        return files
            .map { file ->
                file
                    .lastModified()
                    .toLocalDateTime()
                    .toLocalDate()
            }
            .toSet()
            .toList()
            .sorted()
    }
}
