package com.example.jobtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JobApplication::class], version = 1, exportSchema = false)
abstract class JobDatabase : RoomDatabase() {

    abstract fun jobDao(): JobDao

    companion object {
        @Volatile
        private var Instance: JobDatabase? = null

        fun getDatabase(context: Context): JobDatabase {
            // If the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    JobDatabase::class.java,
                    "job_database"
                )
                    .fallbackToDestructiveMigration() // Wipes data if schema changes (good for dev)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}