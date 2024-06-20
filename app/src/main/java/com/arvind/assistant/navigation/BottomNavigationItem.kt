package com.arvind.assistant.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector


val items  = listOf(
    BottomNavigationItem.Add,
    BottomNavigationItem.Today,
    BottomNavigationItem.Courses,
    BottomNavigationItem.Calendar
)

sealed class BottomNavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount : Int? = null,
){
    data object Add : BottomNavigationItem(
        route = Screen.Add.route,
        title = "Add",
        selectedIcon = Icons.Filled.Add,
        unSelectedIcon = Icons.Outlined.Add,
        hasNews = false
    )
    data object Calendar : BottomNavigationItem(
        route = Screen.Calendar.route,
        title = "Calendar",
        selectedIcon = Icons.Filled.DateRange,
        unSelectedIcon = Icons.Outlined.DateRange,
        hasNews = false
    )
    data object Today : BottomNavigationItem(
        route = Screen.Today.route,
        title = "Today",
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings,
        hasNews = false
    )
    data object Courses : BottomNavigationItem(
        route = Screen.Courses.route,
        title = "Courses",
        selectedIcon = Icons.Filled.AccountCircle,
        unSelectedIcon = Icons.Outlined.AccountCircle,
        hasNews = false
    )
}
