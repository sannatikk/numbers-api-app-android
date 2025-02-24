package com.example.nerdyfactsapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nerdyfactsapp.ui.HomeScreen
import com.example.nerdyfactsapp.ui.NerdyFactsViewModel
import com.example.nerdyfactsapp.ui.SavedFactsScreen

@Composable
// this is the entry point that sets up navigation, is called from MainActivity
fun NerdyFactsApp(viewModel: NerdyFactsViewModel) {

    // create navcontroller to manage screen transitions
    val navController: NavHostController = rememberNavController()

    // scaffold used to provide app layout structure
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MyAppBar(navController) },
        content = { paddingValues ->
            // paddingValues is for displaying screen content defined in AppNavigation
            AppNavigation(navController, viewModel, modifier = Modifier.padding(paddingValues))
        }
    )
}

// navigation graph that switches between screens
// aka which screens exist and how to switch between them
@Composable
fun AppNavigation(navController: NavHostController, viewModel: NerdyFactsViewModel, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") { HomeScreen(viewModel) }
        composable("favorites") { SavedFactsScreen(viewModel) }
    }
}

// dynamic app bar that changes title/icons based on current screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(navController: NavHostController) {

    val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route
    val titleText = if (currentRoute == "home") "Nerdy Facts App" else "Favorite Facts"

    TopAppBar(
        title = {
            Text(
                titleText,
                style = MaterialTheme.typography.headlineLarge) },
        navigationIcon = {
            // show the back arrow only when on the 'favorites' screen
            if (currentRoute == "favorites") {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Home")
                }
            }
        },
        actions = {
            // show the heart icon only when not on the 'favorites' screen
            if (currentRoute != "favorites") {
                IconButton(onClick = {
                    // navigate to the favorites screen and add it to the back stack
                    navController.navigate("favorites") {
                        // ensure we don't create duplicate backstack entries for the favorites screen
                        launchSingleTop = true
                    }
                }) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites")
                }
            }
        }
    )
}


// FILE NOTES
// Interacts with:
// • NavHostController (handles screen switching)
// • NerdyFactsViewModel (passes data to screens)
// Used by:
// • MainActivity.kt (calls NerdyFactsApp() to start the app)
// • HomeScreen.kt & SavedFactsScreen.kt (rendered inside NavHost)