package com.arvind.assistant.screens.courseDetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.arvind.assistant.components.AssistantButton
import com.arvind.assistant.components.AssistantFAB
import com.arvind.assistant.components.ScheduleBottomSheet
import com.arvind.assistant.db.ClassScheduleDetails
import com.arvind.assistant.db.CourseDetails
import com.arvind.assistant.db.ExtraClassTimings
import com.arvind.assistant.screens.courseDetails.components.CourseClassesStatus
import com.arvind.assistant.screens.courseDetails.components.ExtraClassBottomSheet
import com.arvind.assistant.screens.createCourse.components.ScheduleClassListItem
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CourseDetailsScreen(
    course: CourseDetails,
    classes: List<ClassScheduleDetails>,
    scheduleToBeDeleted: (schedule: ClassScheduleDetails) -> Unit,
    onAddScheduleClass: (schedule: ClassScheduleDetails) -> Unit,
    goToAttendanceRecordScreen: () -> Unit,
    onExtraClassCreated: (extraClassTimings: ExtraClassTimings) -> Unit,
    goToCreateAssignmentScreen: () -> Unit
) {
    val showTip = remember{
        mutableStateOf(false)
    }
    val showScheduleOptions = remember {
        mutableStateOf(false)
    }

    val showScheduleBottomSheet = remember {
        mutableStateOf(false)
    }

    val showExtraClassBottomSheet = remember {
        mutableStateOf(false)
    }

    var scheduleItemToBeDeleted: ClassScheduleDetails?  by remember {
        mutableStateOf(null)
    }


    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(text = course.courseName)
                },
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row{
                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Rounded.Edit, contentDescription = "Info")
                        }
                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Rounded.Delete, contentDescription = "Delete")
                        }
                        IconButton(onClick = {
                                goToCreateAssignmentScreen()
                        }) {
                            Icon(Icons.Rounded.ThumbUp, contentDescription = "Delete")
                        }
                    }


                }
            )
        },

        floatingActionButton = {
            AssistantFAB(
                onClick = {
                    showScheduleBottomSheet.value = true
                },
                icon = Icons.Filled.Add,
                text = "Add Schedule Classes"
            )
        }
    ) {paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                CourseClassesStatus(course)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AssistantButton(
                        text = "Attendance Record",
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        goToAttendanceRecordScreen()
                    }
                    AssistantButton(
                        text = "Create extra class",
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        showExtraClassBottomSheet.value = true
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Weekly Schedule",
                        style = MaterialTheme.typography.bodyLarge
                    )



                    IconButton(
                        onClick = {
                            showTip.value = !showTip.value
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = null
                        )
                        if(showTip.value){
                            DropdownMenu(
                                expanded = showTip.value,
                                onDismissRequest = { showTip.value= false },

                            ) {
                                Text(
                                    text = "To add attendance record for a previous class, click on that schedule item ",

                                )
                            }
                        }
                    }
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
                                showScheduleOptions.value = true
                            },
                            onCloseClick = {
//                                viewModel.deleteClassForTheCourse(index)
                            }
                        )

                        if(showScheduleOptions.value){
                            DropdownMenu(
                                expanded = showScheduleOptions.value,
                                onDismissRequest = { showScheduleOptions.value = false },
                                offset = DpOffset(50.dp, 0.dp)
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "Add Attendance Record")
                                    },
                                    onClick = {

                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Text(text = "does not include in schedule")
                                    },
                                    onClick = {

                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "Delete Schedule")
                                    },
                                    onClick = {
                                        scheduleItemToBeDeleted = classDetail
                                    }
                                )

                            }
                        }
                    }
                }
            }

            if(showScheduleBottomSheet.value){
                ScheduleBottomSheet(
                    onDismissRequest = { showScheduleBottomSheet.value = false },
                    onCreateClass = { classScheduleDetails ->
                        onAddScheduleClass(classScheduleDetails)
                    }
                )
            }

            if(scheduleItemToBeDeleted != null){
                scheduleToBeDeleted(scheduleItemToBeDeleted!!)
                scheduleItemToBeDeleted = null
            }

            if(showExtraClassBottomSheet.value){
                ExtraClassBottomSheet(
                    courseName = course.courseName,
                    onDismissRequest = {
                        showExtraClassBottomSheet.value = false
                    },
                    onCreateExtraClass = { extraClassTimings ->
                        onExtraClassCreated(extraClassTimings)
                    }
                )

            }
        }
    }
}

@Preview
@Composable
fun CourseDetailsScreenPreview() {
    CourseDetailsScreen(
        course = CourseDetails(
            courseId = 1,
            courseName = "Course Name",
            requiredAttendance = 75.0,
            currentAttendancePercentage = 80.0
        ),
        classes = listOf(
            ClassScheduleDetails(
                dayOfWeek = LocalDate.now().dayOfWeek,
                startTime = LocalTime.now(),
                endTime = LocalTime.now().plusHours(1),
                scheduleId = 1L,
            )
        ),
        scheduleToBeDeleted = {

        },
        onAddScheduleClass = {

        },
        goToAttendanceRecordScreen = {

        },
        onExtraClassCreated = {

        },
        goToCreateAssignmentScreen = {

        }
    )
}