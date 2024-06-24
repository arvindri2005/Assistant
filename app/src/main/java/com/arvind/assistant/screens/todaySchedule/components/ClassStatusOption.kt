package com.arvind.assistant.screens.todaySchedule.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arvind.assistant.R
import com.arvind.assistant.db.CourseClassStatus

@Composable
fun ClassStatusOptions(
    initialStatus: CourseClassStatus,
    setClassStatus: (CourseClassStatus) -> Unit
) {
    Column(Modifier.selectableGroup()) {
        CourseClassStatus.entries.forEach { newCourseStatus ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (newCourseStatus == initialStatus),
                        onClick = { setClassStatus(newCourseStatus) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (newCourseStatus == initialStatus),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = stringResource(
                        id = when (newCourseStatus) {
                            CourseClassStatus.Present -> R.string.present
                            CourseClassStatus.Absent -> R.string.absent
                            CourseClassStatus.Cancelled -> R.string.cancelled
                            CourseClassStatus.Unset -> R.string.not_set
                        }
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClassStatusOptionsPreview() {
    ClassStatusOptions(CourseClassStatus.Unset) {

    }
}