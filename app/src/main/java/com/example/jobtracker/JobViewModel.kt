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
import com.example.jobtracker.network.WebClient
import kotlinx.coroutines.Dispatchers

class JobViewModel(private val jobDao: JobDao) : ViewModel() {


    val allJobs: StateFlow<List<JobApplication>> = jobDao.getAllJobs()
        .stateIn(
            scope = viewModelScope, // Ties this stream to the ViewModel's lifecycle
            started = SharingStarted.WhileSubscribed(5000), // Keeps connection alive for 5 secs during configuration changes
            initialValue = emptyList() // Default state before the database loads
        )

    // POST endpoint equivalent:
    fun addJob(job: JobApplication) {
        // Dispatchers.IO safely moves this network/database work off the main UI thread
        viewModelScope.launch(Dispatchers.IO) {

            // 1. Fetch raw HTML using your new WebClient service
            val htmlText = WebClient.getHtmlString(job.url)

            // 2. If HTML was successfully fetched, extract the image URL
            // 2. If HTML was successfully fetched, extract the image URL
            val fetchedLogo = if (htmlText != null) {
                WebClient.extractLogo(htmlText, job.url)
            } else {
                null
            }

            // 3. Attach the result to a copy of the job and save it to SQLite
            val finalJobToSave = job.copy(logoUrl = fetchedLogo)
            jobDao.insert(finalJobToSave)
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