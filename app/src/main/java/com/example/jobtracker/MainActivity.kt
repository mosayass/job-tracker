package com.example.jobtracker // Ensure this matches your package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jobtracker.ui.theme.JobTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Initialize the routing engine
                    val navController = rememberNavController()

                    // 2. Define the Router (NavHost) and set the starting screen
                    NavHost(navController = navController, startDestination = "job_list") {

                        // Route 1: The List Screen
                        composable("job_list") {
                            JobListScreen(
                                onNavigateToAddJob = {
                                    // Push the add_job screen onto the stack
                                    navController.navigate("add_job")
                                }
                            )
                        }

                        // Route 2: The Add Job Screen
                        composable("add_job") {
                            AddJobScreen(
                                onNavigateBack = {
                                    // Pop the top screen off the stack to go back
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}