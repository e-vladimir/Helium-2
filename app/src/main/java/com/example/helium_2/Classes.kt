// КЛАССЫ ДАННЫХ

package com.example.helium_2

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.time.LocalDateTime


enum class MIME(val sign: String) {
    IMAGES("image/"), VIDEO("video/")
}


class MediaFile {
    private lateinit var _documentFile: DocumentFile
    private lateinit var _fileTime: LocalDateTime
    private lateinit var _mediaGroup: String
    private lateinit var _mediaTime: String
    private lateinit var _name: String
    private lateinit var _type: String
    private lateinit var _uri: Uri
    private var _isHidden: Boolean = false
    private var _isImage: Boolean = false

    val documentFile: DocumentFile
        get() = _documentFile

    val uri: Uri
        get() = _uri

    val name: String
        get() = _name

    val isHidden: Boolean
        get() = _isHidden

    val isImage: Boolean
        get() = _isImage

    val fileTime: LocalDateTime
        get() = _fileTime

    val mediaTime: String
        get() = _mediaTime

    constructor(documentFile: DocumentFile) {
        updateMediaFile(documentFile)
    }

    fun updateMediaFile(documentFile: DocumentFile?) {
        if (documentFile != null) _documentFile = documentFile

        _uri = _documentFile.uri
        _name = _documentFile.name ?: ""
        _type = _documentFile.type ?: ""
        _isHidden = _name.startsWith(".")
        _isImage = _type.startsWith(MIME.IMAGES.sign)
        _fileTime = _documentFile.lastModified().toLocalDateTime()
        _mediaGroup = _fileTime.toLocalDate().toFormattedString()
        _mediaTime = _fileTime.toFormattedString(includeTime = true)
    }

    fun switchVisible(): Boolean {
        val parentFile: DocumentFile = _documentFile.parentFile ?: return isHidden

        val newFileName: String = when (_name.startsWith(".")) {
            true -> _name.substring(1)
            false -> ".${name}"
        }

        if (!_documentFile.renameTo(newFileName)) return isHidden

        updateMediaFile(parentFile.findFile(newFileName) ?: return isHidden)

        return isHidden
    }

    fun delete(): Boolean {
        return documentFile.delete()
    }
}
