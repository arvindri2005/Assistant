package com.arvind.assistant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.arvind.assistant.CreateCourseScreen
import com.arvind.assistant.HomeScreen
import com.arvind.assistant.SearchScreen
import com.arvind.assistant.SettingsScreen
import com.arvind.assistant.db.DBOps

@Composable
fun BottomNavController(navController: NavHostController) {

    val dbOps: DBOps = DBOps.instance

    NavHost(
        navController = navController,
        startDestination = "Home"
    ){
        composable("Home"){
            HomeScreen {
                navController.navigate("CreateCourse")
            }
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
        composable("CreateCourse"){
            CreateCourseScreen(
                createCourse = { courseName, requiredAttendance ->
                    dbOps.createCourse(courseName, requiredAttendance)
                    navController.popBackStack()
                }
            )
        }
    }
}
