// VIEW-МОДЕЛИ

package com.example.helium_2

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class ViewModelApp : ViewModel() {
    var folders = mutableListOf<FolderInformation>(FolderInformation("DCIM", 10), FolderInformation("Загрузки", 1028))
    var currentFolder = mutableStateOf("")

}
