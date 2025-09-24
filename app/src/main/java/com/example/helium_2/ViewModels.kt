package com.example.helium_2

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class ViewModelApp : ViewModel() {
    var currentPage = mutableIntStateOf(1)
    var currentFolder = mutableStateOf("Камера")
}


val viewModelApp = ViewModelApp()
