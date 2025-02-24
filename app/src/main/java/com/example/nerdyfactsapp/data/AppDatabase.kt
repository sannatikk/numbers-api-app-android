package com.example.nerdyfactsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nerdyfactsapp.data.FactDao
import com.example.nerdyfactsapp.data.SavedFact

// Implements Room as a Singleton to prevent multiple database instances.
// Provides access to the factDao() for CRUD operations.
// Ensures thread safety with @Volatile and synchronized().
// Database file is named "fact_database", making it easy to locate in debugging.

// Database class with a singleton Instance object
@Database(entities = [SavedFact::class], version = 1, exportSchema = false)
abstract class FactDatabase : RoomDatabase() {

    // access to database operations
    abstract fun factDao(): FactDao

    companion object {
        @Volatile
        private var Instance: FactDatabase? = null

        fun getDatabase(context: Context): FactDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FactDatabase::class.java, "fact_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

// Interacts with:
// • SavedFact (defines the table structure).
// • FactDao (provides access to database operations).
// • DefaultAppContainer (injects database dependencies).
// • Used by:
// • FactsRepositoryImplementation (fetches/saves facts using factDao).