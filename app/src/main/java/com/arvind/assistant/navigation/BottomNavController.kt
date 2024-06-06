package com.arvind.assistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arvind.assistant.CreateCourseScreen
import com.arvind.assistant.HomeScreen
import com.arvind.assistant.MyCoursesScreen
import com.arvind.assistant.SearchScreen
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
            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current
            ) {
                MyCoursesScreen(
                    courses = dbOps.getAllCourses()
                        .collectAsStateWithLifecycle(initialValue = listOf()).value,
                )
            }

        }
        composable("CreateCourse"){
            CreateCourseScreen(
                createCourse = { courseName, requiredAttendance ->
                    dbOps.createCourse(courseName, requiredAttendance)
                    navController.popBackStack()
                }
            )
        }

        composable("MyCourses"){
            MyCoursesScreen(
                courses = dbOps.getAllCourses()
                    .collectAsStateWithLifecycle(initialValue = listOf()).value,
            )
        }
    }
}
