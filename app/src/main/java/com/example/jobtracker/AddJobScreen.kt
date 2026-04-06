package com.example.jobtracker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jobtracker.data.JobApplication
import androidx.compose.runtime.saveable.rememberSaveable
// 1. Add the navigation callback parameter
@Composable
fun AddJobScreen(viewModel: JobViewModel, onNavigateBack: () -> Unit) {
    var title by rememberSaveable { mutableStateOf("") }
    var company by rememberSaveable { mutableStateOf("") }
    var url by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = androidx.compose.ui.Alignment.CenterVertically)
    ) {
        Text(text = "Add New Job", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Job Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("Job Posting URL") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                // Basic validation: only save if title and company are provided
                if (title.isNotBlank() && company.isNotBlank()) {
                    // 1. Create the database entity
                    val newJob = JobApplication(
                        title = title,
                        company = company,
                        url = url
                        // ID is 0 (auto-generated) and logoUrl defaults to null
                    )
                    // 2. Execute the POST action on a background thread
                    viewModel.addJob(newJob)
                    // 3. Return to the list screen
                    onNavigateBack()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Save Job")
        }
    }
}