package com.arvind.assistant.screens.courseDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arvind.assistant.components.AssistantButton
import com.arvind.assistant.components.AssistantFAB
import com.arvind.assistant.components.AssistantTextField
import com.arvind.assistant.db.ClassScheduleDetails
import com.arvind.assistant.db.CourseDetails
import com.arvind.assistant.screens.createCourse.components.ScheduleBottomSheet
import com.arvind.assistant.screens.createCourse.components.ScheduleClassListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    course: CourseDetails,
    classes: List<ClassScheduleDetails>
) {
    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(text = course.courseName)
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { it ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {


                Text(
                    text = "Required Attendance: ${course.requiredAttendance}%",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                )

                Text(
                    text = "Current Attendance: ${course.currentAttendancePercentage}%",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                )



                AssistantButton(text = "Add Schedule Classes") {

                }

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    classes.forEachIndexed { index, classDetail ->
                        Spacer(modifier = Modifier.height(8.dp))
                        ScheduleClassListItem(
                            item = classDetail,
                            onClick = {
//                                viewModel.updateClassToUpdateIndex(index)
//                                viewModel.updateBottomSheetState(true)
                            },
                            onCloseClick = {
//                                viewModel.deleteClassForTheCourse(index)
                            }
                        )
                    }
                }


            }

        }
    }
}