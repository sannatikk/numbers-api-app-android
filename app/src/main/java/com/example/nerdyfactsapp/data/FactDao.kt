package com.example.nerdyfactsapp.data
import androidx.room.*

// Defines Data Access Object (DAO) for Room database
// Provides functions for interacting with the SavedFacts table
// Uses Flow for real-time data updates in UI
// Prevents duplicate entries

@Dao
interface FactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // if fact already exists, ignore
    suspend fun insertFact(fact: SavedFact)

    // check if a fact already exists
    // returns null if no match is found
    @Query("SELECT * FROM SavedFacts WHERE fact = :factText LIMIT 1")
    suspend fun getFactByText(factText: String): SavedFact?

    @Query("SELECT * FROM SavedFacts")
    fun getAllFacts(): kotlinx.coroutines.flow.Flow<List<SavedFact>>

    @Query("SELECT * FROM SavedFacts WHERE category = 'number'")
    fun getNumberFacts(): kotlinx.coroutines.flow.Flow<List<SavedFact>>

    @Query("SELECT * FROM SavedFacts WHERE category = 'math'")
    fun getMathFacts(): kotlinx.coroutines.flow.Flow<List<SavedFact>>

    @Query("SELECT * FROM SavedFacts WHERE category = 'year'")
    fun getYearFacts(): kotlinx.coroutines.flow.Flow<List<SavedFact>>

    @Delete
    suspend fun deleteFact(fact: SavedFact)
}

// Interacts with:
// • FactDatabase (provides database instance).
// • SavedFact (defines the table structure).
// Used by:
// •FactsRepositoryImplementation (calls insertFact(), getAllFacts(), etc.).