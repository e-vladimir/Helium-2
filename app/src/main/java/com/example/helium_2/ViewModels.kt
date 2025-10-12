// VIEW-МОДЕЛИ

package com.example.helium_2

import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel


class ViewModelApp : ViewModel() {
    var foldersCounters = mutableMapOf<String, String>("Camera" to "...", "Загрузки" to "182")
    var currentFolder = mutableStateOf("")

}
