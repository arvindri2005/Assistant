package com.how.dailyloger.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.how.dailyloger.HomeScreen
import com.how.dailyloger.SearchScreen
import com.how.dailyloger.SettingsScreen

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
