// МЕНЮ

package com.example.helium_2

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun MenuFolder() {
    var menuExpanded by viewModelApp.menuExpanded
    var forgetFolder by viewModelApp.forgetFolder
    var currentFolder by viewModelApp.currentFolder
    var savingFolder by viewModelApp.savingFolder

    if (forgetFolder) {
        AlertDialog(
            onDismissRequest = {
                forgetFolder = false
            },
            title = { Text(text = "Забыть каталог?") },
            text = { Text(text = "Забыть $currentFolder?") },
            confirmButton = {
                TextButton(onClick = {
                    forgetFolder = false
                    viewModelApp.forgotCurrentFolder()
                    savingFolder = true
                }) { Text("Забыть") }
            },
            dismissButton = {
                TextButton(onClick = { forgetFolder = false }) { Text("Отмена") }
            }
        )
    }


    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false }
    ) {
        DropdownMenuItem(
            onClick = {
                menuExpanded = false
                forgetFolder = true
            },
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) },
            text = { Text("Забыть каталог") }
        )
    }

}