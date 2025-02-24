package com.example.nerdyfactsapp.network
import retrofit2.http.GET
import retrofit2.http.Path

// this is a Retrofit API service interface
// it allows the app to use the numbers api
// uses suspend to make network requests asynchronously
// path url is passed in the "type"

interface FactsApiService {
    @GET("{type}")
    suspend fun getFact(@Path("type") type: String): String // note api response is a string
}

// Interacts with:
// • Retrofit (handles network requests)
// • FactsRepositoryImplementation (calls getFact() for fetching data)
// Used by:
// • FactsRepository (fetches and processes API data before returning it to ViewModel)
// • NerdyFactsViewModel (triggers API requests when needed)