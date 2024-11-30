package com.arvind.assistant.screens.myCourses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arvind.assistant.components.AssistantFAB
import com.arvind.assistant.db.CourseDetails
import com.arvind.assistant.screens.myCourses.components.MyCoursesListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesScreen(
    courses: List<CourseDetails>,
    goToCourseDetails: (courseId: Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Courses")
                },
                modifier = Modifier.offset(y = (-40).dp)
            )
        }
    ){it->
        Surface(
            modifier = Modifier
                .padding(it)
                .offset(y = (-50).dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
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
                    MyCoursesListItem(
                        course = it,
                        onClick = {
                            goToCourseDetails(it.courseId)
                        }
                    )
                }
                item{
                    Spacer (
                        modifier = Modifier.height(100.dp)
                    )
                }
            }
        }
    }
}