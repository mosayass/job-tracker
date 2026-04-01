package com.example.jobtracker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Job(val id: Int, val title: String, val company: String, val status: String)

val dummyJobs = listOf(
    Job(1, "Senior Backend Engineer", "TechCorp", "Applied"),
    Job(2, "AI Engineer", "DigiNova", "Interviewing"),
    Job(3, "Software Architect", "CPath", "Offer Received"),
    Job(4, "Android Developer", "StartupX", "Rejected"),
    Job(5, "Data Scientist", "DataWorks", "Applied")
)

@Composable
fun JobItem(job: Job) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, style = MaterialTheme.typography.titleLarge)
            Text(text = job.company, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Status: ${job.status}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// NOTE the new parameter: onNavigateToAddJob is a function we pass in
@Composable
fun JobListScreen(onNavigateToAddJob: () -> Unit) {
    // Scaffold provides the architectural slot for the floating button
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddJob) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Job")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Apply the padding the Scaffold gives us
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                Text(
                    text = "My Job Applications",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(dummyJobs) { job ->
                JobItem(job = job)
            }
        }
    }
}