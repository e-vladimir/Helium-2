// КЛАССЫ ДАННЫХ

package com.example.helium_2

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.time.LocalDateTime


class MediaFile(val documentFile: DocumentFile) {
    val uri: Uri = documentFile.uri
    val name: String = documentFile.name ?: ""
    val type: String = documentFile.type ?: ""
    val isHidden: Boolean = name.startsWith(".")
    val dateTime: LocalDateTime = documentFile.lastModified().toLocalDateTime()
    val dateGroup: String = dateTime.toLocalDate().toFormattedString()
    val fileTime: String = dateTime.toFormattedString(includeTime = true)


    fun switchVisible(): Boolean {
        return false
    }

    fun delete(): Boolean {
        return false
    }
}
