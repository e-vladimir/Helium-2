// Конверторы данных

package com.example.helium_2

import android.net.Uri


fun uriToString(folderPath: Uri): String =
    folderPath.toString().replace("%20", " ").replace("%21", "!").replace("%23", "#")
        .replace("%24", "").replace("%28", "(").replace("%29", ")").replace("%2F", "/")
        .replace("%3A", ":").replace("%3D", "=").replace("%3F", "?").replace("%40", "@").split(":")
        .last().split("/").last().trim()
