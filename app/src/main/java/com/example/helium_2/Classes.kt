/* Модели данных */

package com.example.helium_2

import android.content.Context
import android.graphics.BitmapFactory
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
    private var _size: Pair<Int, Int> = Pair(0, 0)
    private var _angle: Float = 0f

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

    val size: Pair<Int, Int>
        get() = if (angle in listOf(0f, 180f)) _size else Pair(_size.second, _size.first)

    val ratio: Float
        get() = (if (size.second == 0) 1.000f else size.first / size.second).toFloat()

    var angle: Float
        get() = _angle % 360f
        set(value) {
            _angle = value % 360f
        }

    val isRotated: Boolean
        get() = angle in listOf(90f, 270f)

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

    fun readSize(context: Context, updateMode: Boolean = false) {
        if (updateMode && (size.first > 0)) return

        context.contentResolver.openInputStream(uri)?.use { stream ->
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }

            BitmapFactory.decodeStream(stream, null, options)
            _size = Pair(options.outWidth, options.outHeight)
        }
    }

    fun rotate(toCCW: Boolean = false) {
        _angle = (_angle + 90f * if (toCCW) -1 else 1) % 360f
    }
}
