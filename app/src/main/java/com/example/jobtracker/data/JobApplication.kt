package com.example.jobtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_applications")
data class JobApplication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val company: String,
    val url: String,
    val logoUrl: String? = null,
    val status: String = "Applied" // Default status for new entries
)