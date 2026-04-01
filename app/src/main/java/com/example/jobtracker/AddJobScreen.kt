package com.example.jobtracker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 1. Add the navigation callback parameter
@Composable
fun AddJobScreen(onNavigateBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

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
            // 2. Trigger the callback when clicked
            onClick = { onNavigateBack() },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Save Job")
        }
    }
}