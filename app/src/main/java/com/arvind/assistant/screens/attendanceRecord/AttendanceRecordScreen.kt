package com.arvind.assistant.screens.attendanceRecord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseDetails
import com.arvind.assistant.screens.attendanceRecord.components.AttendanceListItem
import com.arvind.assistant.utils.dateFormatter
import java.time.LocalDate

sealed class HeaderListItem {
    class Header(val value: LocalDate) : HeaderListItem()
    class Item(val value: AttendanceRecordHybrid) : HeaderListItem()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRecordScreen(
    course: CourseDetails,
    records: List<AttendanceRecordHybrid> = listOf()
) {
    var setClassSheetItem: AttendanceRecordHybrid? by remember {
        mutableStateOf(null)
    }
    var itemToBeDeleted: AttendanceRecordHybrid? by remember {
        mutableStateOf(null)
    }

    val lists = remember(records) {
        val finalList = mutableListOf<HeaderListItem>()
        var prevDateTime: LocalDate? = null
        records
            .sortedWith { first, second ->
                val dateCompare = second.date.compareTo(first.date)
                if(dateCompare == 0)
                    second.startTime.compareTo(first.startTime)
                else
                    dateCompare
            }.forEach { item ->
                if (prevDateTime == null || prevDateTime!! != item.date) {
                    finalList.add(HeaderListItem.Header(item.date))
                }
                finalList.add(HeaderListItem.Item(item))
                prevDateTime = item.date
            }
        finalList
    }

    val state = rememberLazyListState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = " ${course.courseName} Attendance Record"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyColumn(
                state = state,
                modifier = Modifier.testTag("dashboard:transaction_list"),
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.Top
                )
            ) {
                itemsIndexed(
                    items = lists,
                    contentType = { _, item ->
                        when (item) {
                            is HeaderListItem.Header -> "Header"
                            is HeaderListItem.Item -> "Item"
                        }
                    }
                ) { index, item ->
                    when (item) {
                        is HeaderListItem.Header -> {
                            Text(
                                text = remember(item.value) {
                                    dateFormatter.format(item.value)
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )

                        }

                        is HeaderListItem.Item -> {
                            val txn = item.value
                            AttendanceListItem(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth(),
                                item = txn,
                                onClick = {
                                    setClassSheetItem = txn

                                }
                            )

                        }
                    }
                }
                item {
                    Spacer(Modifier.height(100.dp))
                }
            }


        }
    }

}

@Preview
@Composable
fun AttendanceRecordScreenPreview() {
    AttendanceRecordScreen(
        course = CourseDetails(
            courseId = 1,
            courseName = "Maths",
            requiredAttendance = 75.0,
            currentAttendancePercentage = 80.0,
        ),

    )
}