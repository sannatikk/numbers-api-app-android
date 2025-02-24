package com.example.nerdyfactsapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nerdyfactsapp.data.SavedFact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// this file contains all the screens in the app
// home screen, which contains buttons to generate facts
// fact view within the home screen, which displays the fact and a save button
// saved facts screen, which displays a list of saved facts
// category dropdown, which allows the user to filter saved facts by category
// saved facts view, which generates a column of cards for each saved fact
// fact card, which displays a fact with a delete button

@Composable
fun HomeScreen(
    viewModel: NerdyFactsViewModel,
) {
    val fact by viewModel.fact.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.padding(26.dp))
            Button(onClick = { viewModel.fetchNumberFact() }) {
                Text("Generate Fact about a Number")
            }
            Button(onClick = { viewModel.fetchMathFact() }) {
                Text("Generate Fact about Math")
            }
            Button(onClick = { viewModel.fetchYearFact() }) {
                Text("Generate Fact about a Year")
            }
            Spacer(modifier = Modifier.padding(26.dp))
            FactView(fact = fact, viewModel = viewModel, snackbarHostState, coroutineScope)
        }
    }
}

@Composable
fun FactView(
    fact: String?,
    viewModel: NerdyFactsViewModel,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
    ) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (!fact.isNullOrEmpty()) {
            Text(text=fact, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(26.dp))
            Button(
                onClick = {
                    viewModel.saveFact()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            "Fact saved! ❤",
                            duration = SnackbarDuration.Short)
                    } },
            ) {
                Text("Save fact to favorites \uD83D\uDDA4")
            }
        }
    }
}

@Composable
fun SavedFactsScreen(
    viewModel: NerdyFactsViewModel) {

    val filteredFacts by viewModel.getFilteredFacts().collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
        ) {
            CategoryDropdown(selectedCategory, viewModel::setCategory)

            if (filteredFacts.isEmpty()) {
                Text("No saved facts.")
            } else {
                SavedFactsView(filteredFacts, viewModel, snackbarHostState, coroutineScope)
            }
        }
    }
}

@Composable
fun CategoryDropdown(selectedCategory: String, onCategorySelected: (String) -> Unit) {

    // map UI names to backend values
    val categoryMap = mapOf(
        "All" to "All",
        "Number" to "number",
        "Math" to "math",
        "Year" to "year"
    )

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(8.dp)) {
        Button(onClick = { expanded = true }) {
            Text(text = "Filter by Category: ${categoryMap.entries.find { it.value == selectedCategory }?.key ?: selectedCategory}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryMap.forEach { (uiName, backendValue) ->
                DropdownMenuItem(
                    text = { Text(text = uiName) },
                    onClick = {
                        onCategorySelected(backendValue)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SavedFactsView(
    savedFacts: List<SavedFact>,
    viewModel: NerdyFactsViewModel,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(savedFacts) { fact ->
            FactCard(fact, viewModel, snackbarHostState, coroutineScope)
        }
    }
}

@Composable
fun FactCard(
    fact: SavedFact,
    viewModel: NerdyFactsViewModel,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = fact.fact, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = {
                viewModel.deleteFact(fact)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "Fact deleted! ✅",
                        duration = SnackbarDuration.Short)
                }
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete fact")
            }
        }
    }
}