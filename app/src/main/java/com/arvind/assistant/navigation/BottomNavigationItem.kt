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
import androidx.compose.ui.res.painterResource
import com.arvind.assistant.R


val items  = listOf(
//    BottomNavigationItem.Add,
    BottomNavigationItem.Today,
    BottomNavigationItem.Courses,
    BottomNavigationItem.Calendar
)

sealed class BottomNavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int,
    val hasNews: Boolean,
    val badgeCount : Int? = null,
){
//    data object Add : BottomNavigationItem(
//        route = Screen.Add.route,
//        title = "Add",
//        selectedIcon = Icons.Filled.Add,
//        unSelectedIcon = Icons.Outlined.Add,
//        hasNews = false
//    )
    data object Calendar : BottomNavigationItem(
        route = Screen.Calendar.route,
        title = "Calendar",
        selectedIcon = R.drawable.calendar,
        unSelectedIcon = R.drawable.calendar,
        hasNews = false
    )
    data object Today : BottomNavigationItem(
        route = Screen.Today.route,
        title = "Today",
        selectedIcon = R.drawable.user,
        unSelectedIcon = R.drawable.user,
        hasNews = false
    )
    data object Courses : BottomNavigationItem(
        route = Screen.Courses.route,
        title = "Courses",
        selectedIcon = R.drawable.course,
        unSelectedIcon = R.drawable.course,
        hasNews = false
    )
}
