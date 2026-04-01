package com.example.jobtracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jobtracker.data.JobApplication
import com.example.jobtracker.data.JobDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//  The Controller

class JobViewModel(private val jobDao: JobDao) : ViewModel() {


    val allJobs: StateFlow<List<JobApplication>> = jobDao.getAllJobs()
        .stateIn(
            scope = viewModelScope, // Ties this stream to the ViewModel's lifecycle
            started = SharingStarted.WhileSubscribed(5000), // Keeps connection alive for 5 secs during configuration changes
            initialValue = emptyList() // Default state before the database loads
        )

    fun addJob(job: JobApplication) {
        viewModelScope.launch {
            jobDao.insert(job)
        }
    }

    fun updateJob(job: JobApplication) {
        viewModelScope.launch {
            jobDao.update(job)
        }
    }

    fun deleteJob(job: JobApplication) {
        viewModelScope.launch {
            jobDao.delete(job)
        }
    }
}

// 2. The Dependency Injection Resolver (Factory)
class JobViewModelFactory(private val jobDao: JobDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobViewModel(jobDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}