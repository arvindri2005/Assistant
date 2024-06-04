package com.arvind.assistant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arvind.assistant.HomeScreen
import com.arvind.assistant.SearchScreen
import com.arvind.assistant.SettingsScreen

@Composable
fun BottomNavController(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "Home"
    ){
        composable("Home"){
            HomeScreen()
        }
        composable("Search"){
            SearchScreen()
        }
        composable("Profile"){
            ProfileScreen()
        }
        composable("Settings") {
            SettingsScreen()
        }
    }
}
