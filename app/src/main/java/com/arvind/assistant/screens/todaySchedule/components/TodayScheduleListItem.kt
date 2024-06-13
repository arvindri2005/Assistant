package com.arvind.assistant.screens.todaySchedule.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arvind.assistant.db.AttendanceCounts
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.utils.timeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScheduleListItem(
    item: AttendanceRecordHybrid,
    attendanceCounts: AttendanceCounts?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OutlinedCard(
        modifier = Modifier
            .animateContentSize()
            .then(modifier),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.startTime.format(timeFormatter),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = item.endTime.format(timeFormatter),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = item.courseName, style = MaterialTheme.typography.titleLarge)
                    if (item is AttendanceRecordHybrid.ExtraClass) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(25),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                text = "Extra Class",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
                val surfaceColor = when (item.classStatus) {
                    CourseClassStatus.Present -> MaterialTheme.colorScheme.primaryContainer
                    CourseClassStatus.Absent -> MaterialTheme.colorScheme.errorContainer
                    CourseClassStatus.Cancelled -> MaterialTheme.colorScheme.surfaceVariant
                    CourseClassStatus.Unset -> MaterialTheme.colorScheme.surfaceVariant
                }
                Surface(
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(25), color = surfaceColor),
                    shape = RoundedCornerShape(25),
                    color = surfaceColor
                ) {
                    Text(
                        modifier = Modifier
                            .minimumInteractiveComponentSize(),
                        text = when (item.classStatus) {
                            CourseClassStatus.Present -> "P"
                            CourseClassStatus.Absent -> "A"
                            CourseClassStatus.Cancelled -> "C"
                            CourseClassStatus.Unset -> "~"
                        },
                        color = when (item.classStatus) {
                            CourseClassStatus.Present -> MaterialTheme.colorScheme.onPrimaryContainer
                            CourseClassStatus.Absent -> MaterialTheme.colorScheme.onErrorContainer
                            CourseClassStatus.Cancelled -> MaterialTheme.colorScheme.onSurfaceVariant
                            CourseClassStatus.Unset -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 4.dp))
//            Text(
//                text = if (attendanceCounts != null)
//                    FutureThingCalculations.getMessageForFuture(
//                        attendanceCounts.present.toInt(),
//                        attendanceCounts.absents.toInt(),
//                        attendanceCounts.requiredPercentage.toInt()
//                    ) else "",
//                style = MaterialTheme.typography.labelMedium
//            )
        }
    }
}