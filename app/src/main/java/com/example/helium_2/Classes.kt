// КЛАССЫ ДАННЫХ

package com.example.helium_2

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
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

    private var _size: Pair<Int, Int> = Pair(0, 0)

    val documentFile: DocumentFile
        get() = _documentFile

    val uri: Uri
        get() = _uri

    val name: String
        get() = _name

    val type: String
        get() = _type

    val isHidden: Boolean
        get() = _isHidden

    val isImage: Boolean
        get() = _isImage

    val fileTime: LocalDateTime
        get() = _fileTime

    val mediaGroup: String
        get() = _mediaGroup

    val mediaTime: String
        get() = _mediaTime

    val size: Pair<Int, Int>
        get() = _size


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

    fun readSize(context: Context) {
        _size = runCatching {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            }
            if (options.outWidth > 0 && options.outHeight > 0) {
                Pair(options.outWidth, options.outHeight)
            } else null
        }.getOrNull() ?: Pair(0, 0)
    }
}
