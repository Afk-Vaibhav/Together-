package com.example.together

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.together.ui.screens.DashboardScreen
import com.example.together.ui.screens.SettingsScreen
import com.example.together.ui.screens.SetupScreen
import com.example.together.ui.theme.TogetherTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            TogetherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TogetherApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun TogetherApp(viewModel: MainViewModel) {
    val isSetupCompleted by viewModel.isSetupCompleted.collectAsState()
    var currentScreen by remember { mutableStateOf("main") }
    
    if (!isSetupCompleted) {
        SetupScreen(viewModel = viewModel)
    } else {
        if (currentScreen == "main") {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToSettings = { currentScreen = "settings" }
            )
        } else if (currentScreen == "settings") {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { currentScreen = "main" }
            )
        }
    }
}
