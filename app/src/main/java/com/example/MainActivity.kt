package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.PranaViewModel
import com.example.ui.screens.MainContainer
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PranaTheme {
                val viewModel: PranaViewModel = viewModel()
                val screenState by viewModel.currentScreen.collectAsState()

                if (screenState == "main") {
                    MainContainer(viewModel)
                } else {
                    SplashScreen(viewModel)
                }
            }
        }
    }
}
