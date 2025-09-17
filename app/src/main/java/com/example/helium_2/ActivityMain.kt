package com.example.helium_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.helium_2.ui.theme.Helium2Theme
import kotlinx.coroutines.launch


class ActivityMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Helium2Theme {
                FrameApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun FrameApp() {
    val pagerState = rememberPagerState(initialPage = 1) { 3 }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = when (pagerState.currentPage) {
                        0 -> "Каталоги"
                        1 -> "Лента"
                        2 -> "Корзина"
                        else -> "..."
                    }
                )
            }
            )
        },
        bottomBar = {
            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                NavigationBarItem(
                    selected = pagerState.currentPage == 0,
                    label = { Text(text = "Каталоги") },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Folder, contentDescription = "Каталоги"
                        )
                    },
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }
                )

                NavigationBarItem(
                    selected = pagerState.currentPage == 1,
                    label = { Text(text = "Лента") },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Photo, contentDescription = "Лента"
                        )
                    },
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } }
                )

                NavigationBarItem(
                    selected = pagerState.currentPage == 2,
                    label = { Text(text = "Корзина") },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Recycling, contentDescription = "Корзина"
                        )
                    },
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(2) } }
                )
            }
        }
    ) { innerPadding ->
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> FrameFolders(modifier = Modifier.padding(innerPadding))
                1 -> FrameMedia(modifier = Modifier.padding(innerPadding))
                2 -> FrameBasket(modifier = Modifier.padding(innerPadding))
            }
        }
    }

}