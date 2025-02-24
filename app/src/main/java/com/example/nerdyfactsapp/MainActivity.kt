package com.example.nerdyfactsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.nerdyfactsapp.ui.NerdyFactsViewModel
import com.example.nerdyfactsapp.ui.navigation.NerdyFactsApp
import com.example.nerdyfactsapp.ui.theme.NerdyFactsAppTheme

// MainActivity is the entry point of the app and overrides the onCreate function
// it handles dependency injection:
// it retrieves AppContainer from NerdyFactsApplication to get the FactsRepository
// it then creates an instance of NerdyFactsViewModel and passes in factsRepository
// lastly it uses setContent to set the app's UI content:
// it uses my theme and a Surface to set the background color
// and then finally calls the NerdyFactsApp composable with the viewModel as a parameter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as NerdyFactsApplication).container
        val viewModel = NerdyFactsViewModel(appContainer.factsRepository)

        setContent {
            NerdyFactsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NerdyFactsApp(viewModel)
                }
            }
        }
    }
}
