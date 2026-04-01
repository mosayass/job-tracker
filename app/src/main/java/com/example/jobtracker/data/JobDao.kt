package com.example.jobtracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    // Acts like an open WebSocket emitting a new list every time the table changes
    @Query("SELECT * FROM job_applications ORDER BY id DESC")
    fun getAllJobs(): Flow<List<JobApplication>>

    @Insert
    suspend fun insert(job: JobApplication)

    @Update
    suspend fun update(job: JobApplication)

    @Delete
    suspend fun delete(job: JobApplication)
}