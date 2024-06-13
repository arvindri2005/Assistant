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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.arvind.assistant.R
import com.arvind.assistant.db.AttendanceCounts
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.screens.todaySchedule.components.TodayScheduleListItem
import com.arvind.assistant.utils.dateFormatter
import com.arvind.assistant.utils.timeFormatter

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
    BaseDialog(
        onDismissRequest = onDismissRequest,
        dialogPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.attendance_status_setter_info,
                todayCourseItem.courseName,
                todayCourseItem.startTime.format(timeFormatter),
                todayCourseItem.endTime.format(timeFormatter),
                if (todayCourseItem is AttendanceRecordHybrid.ExtraClass)
                    stringResource(R.string.attendance_status_setter_info_extra_class)
                else "",
                stringResource(
                    R.string.attendance_status_setter_info_on_date,
                    todayCourseItem.date.format(dateFormatter)
                )
            ),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
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

@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    ),
    onDismissRequest: () -> Unit,
    dialogPadding: PaddingValues = BaseDialogDefaults.dialogMargins,
    contentPadding: PaddingValues = BaseDialogDefaults.contentPadding,
    minWidth: Dp = 280.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        (LocalView.current.parent as? DialogWindowProvider)?.window?.run {
            setDimAmount(BaseDialogDefaults.dimAmount)
            setGravity(Gravity.BOTTOM)
        }
        Box(
            modifier = modifier
                .widthIn(min = minWidth)
                .padding(dialogPadding)
                .semantics { paneTitle = "Dialog" }
        ) {
            Column(
                modifier = Modifier
                    .clip(BaseDialogDefaults.shape)
                    .shadow(elevation = BaseDialogDefaults.elevation)
                    .background(
                        color = backgroundColor,
                        shape = BaseDialogDefaults.shape
                    )
                    .padding(contentPadding),
            ) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    content()
                }
            }
        }
    }
}

@Composable
fun ClassStatusOptions(
    initialStatus: CourseClassStatus,
    setClassStatus: (CourseClassStatus) -> Unit
) {
    Column(Modifier.selectableGroup()) {
        CourseClassStatus.entries.forEach { dayOfWeek ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (dayOfWeek == initialStatus),
                        onClick = { setClassStatus(dayOfWeek) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (dayOfWeek == initialStatus),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = stringResource(
                        id = when (dayOfWeek) {
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

object BaseDialogDefaults {

    val contentPadding = PaddingValues(
        all = 24.dp
    )

    val contentPaddingAlternative = PaddingValues(
        vertical = 24.dp, horizontal = 8.dp
    )

    val dialogMargins = PaddingValues(
        bottom = 12.dp,
        start = 12.dp,
        end = 12.dp
    )

    val shape = RoundedCornerShape(
        size = 26.dp
    )

    const val dimAmount = 0.65F

    val elevation = 1.dp

    const val animDuration = 150

}