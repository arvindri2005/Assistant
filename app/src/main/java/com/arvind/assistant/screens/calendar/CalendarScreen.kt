package com.arvind.assistant.screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arvind.assistant.screens.calendar.components.CalendarEventItem
import com.arvind.assistant.screens.calendar.components.NoReadCalendarPermissionMessage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CalendarScreen(

    viewModel: CalendarViewModel = hiltViewModel()
){

    val state = viewModel.uiState
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val settingVisible = remember{
        mutableStateOf(false)
    }
    val readCalendarPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_CALENDAR
    )

    Scaffold()
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if(readCalendarPermissionState.hasPermission){

                LaunchedEffect(true) {
                    viewModel.onEvent(
                        CalendarViewModelEvent
                            .ReadPermissionChanged(readCalendarPermissionState.hasPermission)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = "Calendar",
                    )
                }

                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    state.events.forEach {(day, events)->
                        item {
                             Column(
                                 verticalArrangement = Arrangement.spacedBy(12.dp)
                             ){
                                 Text(
                                     text = day.substring(0, day.indexOf(",")),

                                 )
                                 events.forEach { event->
                                     CalendarEventItem(
                                         event = event,
                                         onClick = {

                                         }
                                     )
                                 }
                             }
                        }
                    }

                }
            }else{
                NoReadCalendarPermissionMessage(
                    shouldShowRationale = readCalendarPermissionState.shouldShowRationale,
                    context
                ) {
                    readCalendarPermissionState.launchPermissionRequest()
                }
            }
        }
    }


}