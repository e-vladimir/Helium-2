// КЛАССЫ ДАННЫХ

package com.example.helium_2

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.time.LocalDateTime


enum class MIME(val sign: String) {
    IMAGES("image/"),
    VIDEO("video/")
}



class MediaFile(val documentFile: DocumentFile) {
    val uri: Uri = documentFile.uri
    val name: String = documentFile.name ?: ""
    val type: String = documentFile.type ?: ""
    val isHidden: Boolean = name.startsWith(".")
    val isImage: Boolean = type.startsWith(MIME.IMAGES.sign)
    val fileTime: LocalDateTime = documentFile.lastModified().toLocalDateTime()
    val mediaGroup: String = fileTime.toLocalDate().toFormattedString()
    val mediaTime: String = fileTime.toFormattedString(includeTime = true)


    fun switchVisible(): Boolean {
        return false
    }

    fun delete(): Boolean {
        return false
    }
}
