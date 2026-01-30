package com.example.pigeon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pigeon.app.ui.MapScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This makes the app draw behind the status bar and navigation bar
        enableEdgeToEdge()

        setContent {
            // Use the generic MaterialTheme to fix the red error
            androidx.compose.material3.MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // This color comes from the Material system
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    MapScreen()
                }
            }
        }
    }
}