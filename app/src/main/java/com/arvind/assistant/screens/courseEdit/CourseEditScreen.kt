package com.arvind.assistant.screens.courseEdit

import androidx.compose.runtime.Composable
import com.arvind.assistant.db.CourseDetails
import com.arvind.assistant.navigation.Screen

@Composable
fun CourseEditScreen(
    course: CourseDetails,
    onEditCourse: (courseName: String, requiredAttendance:Double ) -> Unit,
    onGoBack: () -> Unit
){

}