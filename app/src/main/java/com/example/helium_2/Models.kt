// МОДЕЛИ

package com.example.helium_2

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel

import java.time.LocalDate


class ViewModelApp : ViewModel() {
    val folders = mutableStateListOf<String>()
    val mediaDTimes = mutableStateListOf<LocalDate>()
    var currentPage = mutableIntStateOf(if (folders.isNotEmpty()) 1 else 0)
    var currentFolder = mutableStateOf("Каталог")
}


val viewModelApp = ViewModelApp()
