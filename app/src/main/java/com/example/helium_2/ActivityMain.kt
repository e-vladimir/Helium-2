package com.example.helium_2

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import com.example.helium_2.ui.theme.Helium2Theme


val viewModelApp = ViewModelApp()


class ActivityMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Helium2Theme { FrameApp() }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AppPreview() {
    Helium2Theme { FrameApp() }
}
