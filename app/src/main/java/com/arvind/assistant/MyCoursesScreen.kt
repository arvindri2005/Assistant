package com.arvind.assistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arvind.assistant.db.CourseDetails

@Composable
fun MyCoursesScreen(
    courses: List<CourseDetails>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement
            .spacedBy(
                8.dp,
                alignment = Alignment.Top
            )
    ) {
        item{
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(courses){
            Column {
                Text(text = it.courseName)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it.requiredAttendance.toString())

            }
        }
        item{
            Spacer (
                modifier = Modifier.height(100.dp)
            )
        }
    }
}