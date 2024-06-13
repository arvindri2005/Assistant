package com.arvind.assistant.screens.todaySchedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arvind.assistant.db.AttendanceCounts
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.screens.todaySchedule.components.TodayScheduleListItem

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

}