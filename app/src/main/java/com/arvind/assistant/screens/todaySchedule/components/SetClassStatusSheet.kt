package com.arvind.assistant.screens.todaySchedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arvind.assistant.R
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.utils.dateFormatter
import com.arvind.assistant.utils.timeFormatter
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetClassStatusSheet(
    todayCourseItem: AttendanceRecordHybrid,
    onDismissRequest: () -> Unit,
    setClasStatus: (CourseClassStatus) -> Unit,
    onDeleteItem: (() -> Unit)? = null
) {
    var newStatus by remember {
        mutableStateOf(todayCourseItem.classStatus)
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text  = stringResource(
                    id = R.string.attendance_status_setter_info,
                    todayCourseItem.courseName
                ),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .weight(1f)
            )
            if(todayCourseItem is AttendanceRecordHybrid.ExtraClass){
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            .weight(1f),
                        text = "Extra Class",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
//
//        Text(
//            text = stringResource(
//                id = R.string.attendance_status_setter_info,
//                todayCourseItem.courseName,
//                todayCourseItem.startTime.format(timeFormatter),
//                todayCourseItem.endTime.format(timeFormatter),
//                if (todayCourseItem is AttendanceRecordHybrid.ExtraClass)
//                    stringResource(R.string.attendance_status_setter_info_extra_class)
//                else "",
//                stringResource(
//                    R.string.attendance_status_setter_info_on_date,
//                    todayCourseItem.date.format(dateFormatter)
//                )
//            ),
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(16.dp)
//        )
        Spacer(modifier = Modifier.height(16.dp))
        ClassStatusOptions(newStatus) { newStatus = it }
        Row(modifier = Modifier.fillMaxWidth()) {
            if (onDeleteItem != null) {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        onDeleteItem()
                        onDismissRequest()
                    }
                ) {
                    Text(text = "Delete Record")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
            TextButton(onClick = {
                setClasStatus(newStatus)
                onDismissRequest()
            }) {
                Text(text = "ok")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SetClassStatusSheetPreview() {
    SetClassStatusSheet(
        todayCourseItem = AttendanceRecordHybrid.ScheduledClass(
            attendanceId = 1,
            scheduleId = 1,
            courseId = 1,
            courseName = "Maths",
            startTime = LocalTime.now(),
            endTime = LocalTime.now(),
            date = LocalDate.now(),
            classStatus = CourseClassStatus.Present
        ),
        onDismissRequest = {},
        setClasStatus = {}
    )
}