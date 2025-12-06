// КОНВЕРТАЦИЯ ДАННЫХ

package com.example.helium_2

import android.net.Uri

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import java.util.Locale


fun uriToString(folderPath: Uri): String =
    folderPath.toString().replace("%20", " ").replace("%21", "!").replace("%23", "#")
        .replace("%24", "").replace("%28", "(").replace("%29", ")").replace("%2F", "/")
        .replace("%3A", ":").replace("%3D", "=").replace("%3F", "?").replace("%40", "@").split(":")
        .last().split("/").last().trim()


fun LocalDateTime.toFormattedString(includeTime: Boolean = false): String {
    val formatter = DateTimeFormatter.ofPattern(if (includeTime) "dd MMMM yyyy HH:mm" else "dd MMMM yyyy", Locale.getDefault())
    return this.format(formatter).toString()
}


fun LocalDate.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return this.format(formatter).toString()
}


fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this), ZoneId.systemDefault()
    )
}


fun Boolean.toInt() : Int = if (this) 1 else 0
