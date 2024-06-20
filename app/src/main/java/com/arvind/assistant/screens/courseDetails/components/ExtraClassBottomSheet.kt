package com.arvind.assistant.screens.courseDetails.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.arvind.assistant.R
import com.arvind.assistant.db.ExtraClassTimings
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExtraClassBottomSheet(
    courseName: String,
    onDismissRequest: () -> Unit,
    onCreateExtraClass: (ExtraClassTimings) -> Unit,
) {
    var state by rememberSaveable {
        mutableStateOf(ExtraClassTimings.defaultTimeAdjusted())
    }

    ModalBottomSheet(onDismissRequest =  onDismissRequest ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Select date, start time and end time for the new extra class for $courseName",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            val tabs = stringArrayResource(id = R.array.add_extra_class_bottom_sheet_tabs)
            val pagerState = rememberPagerState(pageCount = { tabs.size })
            val scope = rememberCoroutineScope()

            TabRow(selectedTabIndex = pagerState.currentPage) {
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
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.heightIn(min = with(LocalDensity.current) { pagerMinSize.toDp() })
            ) { page ->
                when (page) {
                    0 -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged { pagerMinSize = maxOf(pagerMinSize, it.height) },
                            contentAlignment = Alignment.Center
                        ) {
                            val datePickerState = rememberDatePickerState(
                                initialSelectedDateMillis = state.date.atStartOfDay().toInstant(
                                    ZoneOffset.UTC
                                ).toEpochMilli()
                            )
                            LaunchedEffect(datePickerState.selectedDateMillis) {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    state = state.copy(
                                        date = Instant.ofEpochMilli(millis)
                                            .atZone(ZoneOffset.systemDefault())
                                            .toLocalDate()
                                    )
                                }
                            }
                            DatePicker(state = datePickerState)

                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged { pagerMinSize = maxOf(pagerMinSize, it.height) },
                            contentAlignment = Alignment.Center
                        ) {
                            val timePickerState = rememberTimePickerState(
                                initialHour = if (page == 1) state.startTime.hour else state.endTime.hour,
                                initialMinute = if (page == 1) state.startTime.minute else state.endTime.minute
                            )
                            val context = LocalContext.current
                            LaunchedEffect(timePickerState.hour, timePickerState.minute) {
                                state = if (page == 1) {
                                    val newStart = state.startTime.withHour(timePickerState.hour)
                                        .withMinute(timePickerState.minute)
                                    state.copy(
                                        startTime = newStart,
                                        endTime = newStart.plusHours(1)
                                    )
                                } else {
                                    val newEnd = state.endTime.withHour(timePickerState.hour)
                                        .withMinute(timePickerState.minute)
                                    if (newEnd > state.startTime) {
                                        state.copy(
                                            endTime = newEnd
                                        )
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "End time should be after start time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        state
                                    }
                                }
                            }
                            TimePicker(state = timePickerState)
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
                TextButton(onClick = {
                    onCreateExtraClass(state)
                    onDismissRequest()
                }) {
                    Text(text = "OK")
                }
            }
        }

    }

}