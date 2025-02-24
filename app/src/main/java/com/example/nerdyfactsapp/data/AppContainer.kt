package com.example.nerdyfactsapp.data

import android.content.Context
import com.example.nerdyfactsapp.network.FactsApiService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

// Manages dependency injection for both network and database layers.
// Creates a single source of truth (factsRepository) for fetching and storing facts.
// Ensures lazy initialization of dependencies, improving app performance.
// Easily testable because AppContainer can be replaced with a mock implementation.

// AppContainer defines a contract that any app container must provide a factsRepository
// it allows flexibility in case I need to replace DefaultAppContainer with a different implementation
// (e.g.) for testing
interface AppContainer {
    val factsRepository: FactsRepository
}

// DefaultAppContainer implements AppContainer and injects dependencies for:
// - networking (Retrofit + FactsApiService)
// - database (Room + FactDao)
// - repository (FactsRepository)

class DefaultAppContainer(context: Context) : AppContainer {

    // Retrofit setup
    private val baseUrl = "http://numbersapi.com/random/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(ScalarsConverterFactory.create()) // for plain text response
        .build()

    // factsApiService is initialized lazily to improve performance
    private val factsApiService: FactsApiService by lazy {
        retrofit.create(FactsApiService::class.java)
    }

    // Room database setup
    // retrieves an instance of FactDatabase (Singleton)
    private val database: FactDatabase by lazy {
        FactDatabase.getDatabase(context)
    }
    // gets an instance of factDao to provide access to the database
    private val factDao: FactDao by lazy {
        database.factDao()
    }

    // repository injection
    // injects both network and database into FactsRepositoryImplementation
    // ensures the repository can access both online and locally saved facts
    override val factsRepository: FactsRepository by lazy {
        FactsRepositoryImplementation(factsApiService, factDao)
    }
}

// FILE SUMMARY:

// interacts with:
// - network (Retrofit + FactsApiService) for fetching facts online
// - database (Room + FactDao) for storing and retrieving facts locally
// - FactsRepositoryImplementation provides the repository's functionality
// used by:
// - NerdyFactsApplication to provide dependencies globally
// - NerdyFactsViewModel, which depends on factsRepository
