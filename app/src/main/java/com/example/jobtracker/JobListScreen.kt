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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
@Composable
fun JobItem(
    job: JobApplication,
    onDeleteClick: () -> Unit,
    onStatusChange: (String) -> Unit
) {
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
                JobDetailsText(
                    title = job.title,
                    company = job.company,
                    currentStatus = job.status,
                    onStatusSelected = onStatusChange
                )
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
                    onDeleteClick = { viewModel.deleteJob(job) },
                    onStatusChange = { newStatus ->
                        // Create a mutated copy of the entity and fire it to the update endpoint
                        val updatedJob = job.copy(status = newStatus)
                        viewModel.updateJob(updatedJob)
                    }
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
private fun JobDetailsText(
    title: String,
    company: String,
    currentStatus: String,
    onStatusSelected: (String) -> Unit
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Text(text = company, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        // Replaced static text with our interactive selector
        StatusSelector(currentStatus = currentStatus, onStatusSelected = onStatusSelected)
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
@Composable
private fun StatusSelector(currentStatus: String, onStatusSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("Applied", "Interview", "Rejected", "Accepted")

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        TextButton(onClick = { expanded = true }) {
            Text(text = "Status: $currentStatus")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            statuses.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status) },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}