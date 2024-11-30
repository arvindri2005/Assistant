package com.arvind.assistant.screens.courseDetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
    onGoBack: () -> Unit = {},
    course: CourseDetails,
    classes: List<ClassScheduleDetails>,
    scheduleToBeDeleted: (schedule: ClassScheduleDetails) -> Unit,
    onAddScheduleClass: (schedule: ClassScheduleDetails) -> Unit,
    goToAttendanceRecordScreen: () -> Unit,
    onExtraClassCreated: (extraClassTimings: ExtraClassTimings) -> Unit,
    onDeleteCourse: (Long) -> Unit,
    goToCourseEditScreen: () -> Unit
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

    val showDeleteCourseDialog = remember {
        mutableStateOf(false)
    }


    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(text = course.courseName)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onGoBack()
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
                            showDeleteCourseDialog.value = true
                        }) {
                            Icon(Icons.Rounded.Delete, contentDescription = "Delete")
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
                    text = "Required Attendance: ${course.requiredAttendance.toInt()}%",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                )

                Text(
                    text = "Current Attendance: ${course.currentAttendancePercentage.toInt()}%",
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
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)

                            ) {
                                Text(
                                    text = "To add attendance record for a previous class, click on that schedule item ",
                                    modifier = Modifier.padding(8.dp)

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
                                scheduleItemToBeDeleted = classDetail
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

            if (showDeleteCourseDialog.value) {
                DeleteCourseDialog(
                    courseName = course.courseName,
                    onDismissRequest = { showDeleteCourseDialog.value = false },
                    onDeleteCourse = {
                        onDeleteCourse(course.courseId)
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
        onDeleteCourse = {

        },
        goToCourseEditScreen = {

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteCourseDialog(
    onDismissRequest: () -> Unit,
    courseName: String,
    onDeleteCourse: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Delete Course",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Are you sure you want to delete $courseName?",
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        onDeleteCourse()
                        onDismissRequest()
                    }) {
                        Text(text = "Delete")
                    }
                }
            }
        },
        modifier = Modifier
            .background(shape = RoundedCornerShape(8.dp), color = Color.White)
    )
}
