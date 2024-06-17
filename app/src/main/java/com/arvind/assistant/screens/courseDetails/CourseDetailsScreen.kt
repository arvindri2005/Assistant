package com.arvind.assistant.screens.courseDetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CourseDetailsScreen(
    courseId: String? = null
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(text ="Course Details Screen & Course ID: $courseId")

    }
}