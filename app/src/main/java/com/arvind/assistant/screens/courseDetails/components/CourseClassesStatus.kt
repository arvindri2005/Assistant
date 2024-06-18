package com.arvind.assistant.screens.courseDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arvind.assistant.db.CourseDetails

@Composable
fun CourseClassesStatus(
    course: CourseDetails
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Presents : ${course.presents}",
            )

            Text(
                text = "Absents : ${course.absents}",
            )

            Text(
                text = "Cancelled Classes : ${course.cancels}",
            )
        }
    }
}

@Preview
@Composable
fun CourseClassesStatusPreview(){
    CourseClassesStatus(
        course = CourseDetails(
            courseName = "Maths",
            presents = 10,
            absents = 2,
            requiredAttendance = 75.0,
            currentAttendancePercentage = 80.0,
            courseId = 2
        )
    )
}