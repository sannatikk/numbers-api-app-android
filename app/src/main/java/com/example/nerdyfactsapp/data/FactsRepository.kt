package com.example.nerdyfactsapp.data

import com.example.nerdyfactsapp.network.FactsApiService
import kotlinx.coroutines.flow.Flow

// this file manages data retrieval for the app
// by combining api calls (retrofit) and local database access (room dao)
// the interface sets the rules for how data is fetched and stored
// and the implementation handles the actual fetching and storage
// uses Flow for real-time database updates.
// provides a single source of truth for the ViewModel

interface FactsRepository {

    // network functions
    suspend fun getMathFact(): String
    suspend fun getNumberFact(): String
    suspend fun getYearFact(): String

    // database functions
    suspend fun insertFact(fact: SavedFact)
    suspend fun deleteFact(fact: SavedFact)
    suspend fun getFactByText(factText: String): SavedFact? // allows null return
    fun getAllSavedFacts(): Flow<List<SavedFact>>
    fun getMathFacts(): Flow<List<SavedFact>>
    fun getNumberFacts(): Flow<List<SavedFact>>
    fun getYearFacts(): Flow<List<SavedFact>>
}

class FactsRepositoryImplementation(
    private val apiService: FactsApiService,
    private val factDao: FactDao
) : FactsRepository {

    // network methods

    override suspend fun getMathFact(): String {
        return apiService.getFact("math")
    }
    override suspend fun getNumberFact(): String {
        return apiService.getFact("trivia")
    }
    override suspend fun getYearFact(): String {
        return apiService.getFact("year")
    }

    // database methods

    override suspend fun getFactByText(factText: String): SavedFact? {
        return factDao.getFactByText(factText)
    }
    override suspend fun insertFact(fact: SavedFact) {
        factDao.insertFact(fact)
    }
    override suspend fun deleteFact(fact: SavedFact) {
        factDao.deleteFact(fact)
    }
    override fun getAllSavedFacts(): Flow<List<SavedFact>> {
        return factDao.getAllFacts()
    }
    override fun getMathFacts(): Flow<List<SavedFact>> {
        return factDao.getMathFacts()
    }
    override fun getNumberFacts(): Flow<List<SavedFact>> {
        return factDao.getNumberFacts()
    }
    override fun getYearFacts(): Flow<List<SavedFact>> {
        return factDao.getYearFacts()
    }
}

// interacts with:
// • FactsApiService (fetches facts from API).
// • FactDao (performs database operations).
// • SavedFact (defines fact structure).
// used by:
// • NerdyFactsViewModel (calls repository methods to fetch & store facts).
// • AppContainer (provides the repository as a dependency).

// NOTES: Why use a repository?
// Encapsulation: Keeps data logic separate from UI logic.
// Abstraction: ViewModels and UI don’t need to know where data comes from.
// Single Source of Truth: Ensures consistent data across the app.
// Easier Testing: You can easily swap real data sources with mock versions.
// Code Reusability: Any part of the app can access data via the repository.