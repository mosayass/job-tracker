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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.jobtracker.data.JobApplication
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment

@Composable
fun JobItem(job: JobApplication, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompanyLogo(logoUrl = job.logoUrl)

            Box(modifier = Modifier.weight(1f)) {
                JobDetailsText(title = job.title, company = job.company, status = job.status)
            }

            DeleteActionIcon(onClick = onDeleteClick)
        }
    }
}

@Composable
fun JobListScreen(viewModel: JobViewModel, onNavigateToAddJob: () -> Unit) {
    // 1. Subscribe to the database flow. This automatically triggers a UI redraw when data changes.
    val jobList by viewModel.allJobs.collectAsState()
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
            items(jobList) { job ->
                JobItem(
                    job = job,
                    onDeleteClick = { viewModel.deleteJob(job) }
                )
            }
        }
    }
}
@Composable
private fun CompanyLogo(logoUrl: String?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(logoUrl)
            .crossfade(true)
            .error(android.R.drawable.ic_menu_report_image)
            .build(),
        contentDescription = "Company Logo",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
private fun JobDetailsText(title: String, company: String, status: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Text(text = company, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Status: $status", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
    }
}
@Composable
private fun DeleteActionIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Job",
            tint = MaterialTheme.colorScheme.error
        )
    }
}