package com.example.nerdyfactsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nerdyfactsapp.data.FactsRepository
import com.example.nerdyfactsapp.data.SavedFact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// this view model manages ui-related data for fetching and saving facts
// manages all fact-related logic
// it interacts with the FactsRepository to:
// - fetch facts from the API
// - store and retrieve facts from the database
// - handle category filtering
// - manage ui state using stateflow

class NerdyFactsViewModel(private val factsRepository: FactsRepository) : ViewModel() {

    // private mutable data
    // and its corresponding public read-only state

    private val _fact = MutableStateFlow("")
    val fact: StateFlow<String> = _fact.asStateFlow()

    private val _currentCategory = MutableStateFlow("")
    val currentCategory: StateFlow<String> = _currentCategory.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _savedFacts = MutableStateFlow<List<SavedFact>>(emptyList())
    val savedFacts: StateFlow<List<SavedFact>> = _savedFacts.asStateFlow()

    // auto loads from db on startup
    init {
        loadSavedFacts()
    }

    fun fetchMathFact() {
        viewModelScope.launch {
            _fact.value = factsRepository.getMathFact()
            _currentCategory.value = "math"

        }
    }

    fun fetchNumberFact() {
        viewModelScope.launch {
            _fact.value = factsRepository.getNumberFact()
            _currentCategory.value = "number"
        }
    }

    fun fetchYearFact() {
        viewModelScope.launch {
            _fact.value = factsRepository.getYearFact()
            _currentCategory.value = "year"

        }
    }

    private fun loadSavedFacts(){
        viewModelScope.launch {
            factsRepository.getAllSavedFacts().collect{
                _savedFacts.value = it
            }
        }
    }

    fun saveFact() {
        val factText = fact.value
        val category = currentCategory.value

        if (factText.isNotEmpty() && category.isNotEmpty()) {
            viewModelScope.launch {
                // check if the fact already exists in the database to avoid duplicates
                val existingFact = factsRepository.getFactByText(factText)
                if (existingFact == null) { // only insert if it's not already saved
                    val newFact = SavedFact(fact = factText, category = category)
                    factsRepository.insertFact(newFact)
                    loadSavedFacts() // update the saved facts list
                }
            }
        }
    }

    fun deleteFact(fact: SavedFact){
        viewModelScope.launch {
            factsRepository.deleteFact(fact)
            loadSavedFacts()
        }
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    // filters saved facts dynamically based on selected category
    // uses flatmaplatest to react to category changes, this requires opt-in
    // returns stateflow for ui observation
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun getFilteredFacts(): StateFlow<List<SavedFact>> {
        return selectedCategory.flatMapLatest { category ->
            savedFacts.map { facts ->
                if (category == "All") facts
                else facts.filter { it.category == category }
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

}

// Interacts with:
// • FactsRepository (fetches API data & handles database queries)
// • HomeScreen.kt (triggers fetching & saving facts)
// • SavedFactsScreen.kt (displays & filters saved facts)
// Used by:
// • AppNavigation.kt (passes viewModel to screens)
// • NerdyFactsApp.kt (the main UI wrapper)

// NOTES ON STATEFLOW:
// designed to hold state and allow ui components to observe changes reactively
// used to store and update ui-related data
// always holds a value (incl default), always causes a recomposition on value change
// declared using _name = MutableStateFlow(initialValue), exposed by _name.asStateFlow()