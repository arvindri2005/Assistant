package com.arvind.assistant.components

import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arvind.assistant.R
import com.arvind.assistant.db.ClassScheduleDetails
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleBottomSheet(
    initialState: ClassScheduleDetails? = null,
    onDismissRequest: () -> Unit,
    onCreateClass: (ClassScheduleDetails)-> Unit
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest =onDismissRequest,
        sheetState = sheetState
    ) {
        var selectedWeekDay = rememberSaveable {
            mutableStateOf(initialState?.dayOfWeek ?: LocalDate.now().dayOfWeek)
        }
        var context = LocalContext.current
        var startTime = rememberSaveable(
            saver = TimePickerState.Saver()
        ) {
            TimePickerState(
                initialHour = initialState?.startTime?.hour ?: 0,
                initialMinute = initialState?.startTime?.minute ?: 0,
                is24Hour = DateFormat.is24HourFormat(context),
            )
        }

        var endTime = rememberSaveable(
            saver = Saver(
                save = {
                    listOf(
                        it.value.hour,
                        it.value.minute,
                        it.value.is24hour
                    )
                },
                restore = { value ->
                    mutableStateOf(
                        TimePickerState(
                            initialHour = value[0] as Int,
                            initialMinute = value[1] as Int,
                            is24Hour = value[2] as Boolean
                        )
                    )
                }
            )
        ) {
            mutableStateOf(
                TimePickerState(
                    initialHour = initialState?.endTime?.hour ?: 0,
                    initialMinute = initialState?.endTime?.minute ?: 0,
                    is24Hour = DateFormat.is24HourFormat(context),
                )
            )
        }

        LaunchedEffect(startTime.hour, startTime.minute) {
            endTime.value = TimePickerState(
                initialHour = (startTime.hour + 1) % 24,
                initialMinute = startTime.minute,
                is24Hour = DateFormat.is24HourFormat(context),
            )
        }

        LaunchedEffect(endTime.value.hour, endTime.value.minute) {
            val newEnd = LocalTime.of(endTime.value.hour, endTime.value.minute)
            val start = LocalTime.of(startTime.hour, startTime.minute)
            if (newEnd <= start) {
                Toast.makeText(
                    context,
                    "End time should be after start time",
                    Toast.LENGTH_SHORT
                ).show()
                endTime.value= TimePickerState(
                    initialHour = (startTime.hour + 1) % 24,
                    initialMinute = startTime.minute,
                    is24Hour = DateFormat.is24HourFormat(context),
                )
            }
        }

        Text(
            text = "Select Schedule Details",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        val tabs = stringArrayResource(id = R.array.add_schedule_class_bottom_sheet_tabs)
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        val scope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex =pagerState.currentPage
        ) {
            tabs.forEachIndexed { index, tabName ->
                Tab(
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Text(text = tabName)
                    },
                    selected = pagerState.currentPage == index
                )
            }
        }
        var pagerMinSize by remember {
            mutableIntStateOf(0)
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            item{
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.heightIn(min = with(LocalDensity.current) { pagerMinSize.toDp() })
                ) { page ->
                    when (page) {
                        0 -> {
                            Column(
                                Modifier
                                    .selectableGroup()
                                    .onSizeChanged { pagerMinSize = maxOf(pagerMinSize, it.height) }
                            ){
                                DayOfWeek.entries.forEach { dayOfWeek ->
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(56.dp)
                                            .selectable(
                                                selected = (selectedWeekDay.value == dayOfWeek),
                                                onClick = { selectedWeekDay.value = dayOfWeek },
                                                role = Role.RadioButton
                                            )
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = (dayOfWeek == selectedWeekDay.value),
                                            onClick = null // null recommended for accessibility with screen readers
                                        )
                                        Text(
                                            text = dayOfWeek.getDisplayName(
                                                TextStyle.FULL,
                                                Locale.getDefault()
                                            ),
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                        1 -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onSizeChanged { pagerMinSize = maxOf(pagerMinSize, it.height) },
                                contentAlignment = Alignment.Center
                            ) {

                                TimePicker(state = startTime)
                            }
                        }
                        2 -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onSizeChanged { pagerMinSize = maxOf(pagerMinSize, it.height) },
                                contentAlignment = Alignment.Center
                            ) {
                                TimePicker(state = endTime.value)
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
            , horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
            TextButton(onClick = {
                onCreateClass(
                    ClassScheduleDetails(
                        dayOfWeek = selectedWeekDay.value,
                        startTime = LocalTime.of(startTime.hour, startTime.minute),
                        endTime = LocalTime.of(endTime.value.hour, endTime.value.minute)
                    )
                )
                onDismissRequest()
            }, modifier = Modifier.testTag("sheet_add_class_button")
            ) {
                Text(text = "Ok")
            }
        }

    }
}


@Preview
@Composable
fun PreviewScheduleBottomSheet(){
    ScheduleBottomSheet(
        onDismissRequest = {},
        onCreateClass = {}
    )
}
