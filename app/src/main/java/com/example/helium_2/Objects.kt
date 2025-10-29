// ОБЪЕКТЫ

package com.example.helium_2

import android.content.Context
import android.net.Uri

import androidx.documentfile.provider.DocumentFile


enum class MIME(val sign: String) {
    IMAGES("image/"),
    VIDEO("video/")
}


class FolderProcessor(val folderPath: Uri) {
    var files = listOf<DocumentFile>()

    fun readFiles(context: Context): Int? {
        try {
            files = DocumentFile
                .fromTreeUri(context, folderPath)
                ?.listFiles()!!
                .filter { documentFile -> documentFile.type?.startsWith(MIME.IMAGES.sign) == true }
                .toList()

            return countFiles()
        } catch (_: Exception) {
        }

        return null
    }

    fun countFiles(): Int = files.count()
}
