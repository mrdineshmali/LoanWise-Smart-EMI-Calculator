package com.dineshmali.smartemicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.dineshmali.smartemicalculator.navigation.NavGraph
import com.dineshmali.smartemicalculator.ui.theme.SmartEmiTheme
import com.dineshmali.smartemicalculator.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as SmartEmiApplication
        val repository = app.repository

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val settingsState by settingsViewModel.uiState.collectAsState()

            SmartEmiTheme(darkTheme = settingsState.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        repository = repository,
                        isDarkMode = settingsState.isDarkMode,
                        onToggleDarkMode = settingsViewModel::toggleDarkMode
                    )
                }
            }
        }
    }
}
