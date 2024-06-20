package com.arvind.assistant.screens.attendanceRecord.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.utils.timeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceListItem(modifier: Modifier, item: AttendanceRecordHybrid, onClick: () -> Unit) {
    OutlinedCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier.padding(8.dp),
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
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (item is AttendanceRecordHybrid.ScheduledClass) "Scheduled Class"
                else "Extra Class",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
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
    }
}