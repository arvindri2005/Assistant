package com.arvind.assistant.screens.todaySchedule

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arvind.assistant.components.AssistantButton
import com.arvind.assistant.db.AttendanceCounts
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.screens.todaySchedule.components.SetClassStatusSheet
import com.arvind.assistant.screens.todaySchedule.components.TodayScheduleListItem
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScheduleScreen(
    todaySchedule :List<Pair<AttendanceRecordHybrid, AttendanceCounts>>,
    onSetClassStatus: (item: AttendanceRecordHybrid, newStatus: CourseClassStatus) -> Unit
) {
    val viewModel: TodayScheduleViewModel = hiltViewModel()

    var setClassSheetItem: AttendanceRecordHybrid? by remember {
        mutableStateOf(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Today's Schedule") },
                modifier = Modifier
                    .offset(y = (-40).dp)
            )
        }
    ) {it->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .offset(y = (-40).dp)

        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 5.dp),
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.Top
                )
            ) {
//            item{
//                AssistantButton(text = "Notification") {
//                    viewModel.setAlarm(todaySchedule[0].first.courseId, todaySchedule[0].first.courseName)
//                }
//            }
//            item {
//                Spacer(modifier = Modifier.height(20.dp))
//            }
                items(
                    todaySchedule,
                ) { classItem ->
                    TodayScheduleListItem(
                        item = classItem.first,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            setClassSheetItem = classItem.first
                        },
                        attendanceCounts = classItem.second
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

        }

        if (setClassSheetItem != null) {
            SetClassStatusSheet(
                todayCourseItem = setClassSheetItem!!,
                onDismissRequest = { setClassSheetItem = null },
                setClasStatus = { onSetClassStatus(setClassSheetItem!!, it) }
            )
        }
    }


}

@Preview
@Composable
fun TodayScheduleScreenPreview() {
    TodayScheduleScreen(
        todaySchedule = listOf(
            AttendanceRecordHybrid.ScheduledClass(
                courseId = 2,
                scheduleId = 2,
                attendanceId = 2,
                courseName = "Science",
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(11, 0),
                date = LocalDate.now(),
                classStatus = CourseClassStatus.Absent
            ) to AttendanceCounts(1.0, 0, 1, 1, 1, 75.0),
            AttendanceRecordHybrid.ExtraClass(
                courseId = 2,
                extraClassId = 2,
                courseName = "Science",
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(11, 0),
                date = LocalDate.now(),
                classStatus = CourseClassStatus.Absent
            ) to AttendanceCounts(1.0, 0, 1, 1, 1, 75.0),
            AttendanceRecordHybrid.ExtraClass(
                courseId = 2,
                extraClassId = 2,
                courseName = "Science",
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(11, 0),
                date = LocalDate.now(),
                classStatus = CourseClassStatus.Absent
            ) to AttendanceCounts(1.0, 0, 1, 1, 1, 75.0),

            AttendanceRecordHybrid.ExtraClass(
                courseId = 2,
                extraClassId = 2,
                courseName = "Science",
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(11, 0),
                date = LocalDate.now(),
                classStatus = CourseClassStatus.Absent
            ) to AttendanceCounts(1.0, 0, 1, 1, 1, 75.0),

            AttendanceRecordHybrid.ExtraClass(
                courseId = 2,
                extraClassId = 2,
                courseName = "Science",
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(11, 0),
                date = LocalDate.now(),
                classStatus = CourseClassStatus.Absent
            ) to AttendanceCounts(1.0, 0, 1, 1, 1, 75.0),
        ),
        onSetClassStatus = { _, _ -> }
    )
}