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
    var files = listOf<MediaFile>()

    fun readFiles(context: Context): Int? {
        try {
            files = DocumentFile
                .fromTreeUri(context, folderPath)
                ?.listFiles()!!
                .map { MediaFile(it) }
                .filter { it.type.startsWith(MIME.IMAGES.sign) }
                .toList()

            return countFiles()
        } catch (_: Exception) {
        }

        return null
    }

    fun countFiles(): Int = files.count()

}
