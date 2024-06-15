package com.arvind.assistant.screens.todaySchedule

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.arvind.assistant.R
import com.arvind.assistant.db.AttendanceCounts
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.screens.todaySchedule.components.SetClassStatusSheet
import com.arvind.assistant.screens.todaySchedule.components.TodayScheduleListItem
import com.arvind.assistant.utils.dateFormatter
import com.arvind.assistant.utils.timeFormatter
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TodayScheduleScreen(
    todaySchedule :List<Pair<AttendanceRecordHybrid, AttendanceCounts>>,
    onSetClassStatus: (item: AttendanceRecordHybrid, newStatus: CourseClassStatus) -> Unit
) {

    var setClassSheetItem: AttendanceRecordHybrid? by remember {
        mutableStateOf(null)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.Top
            )
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
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