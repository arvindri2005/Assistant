package com.arvind.assistant.screens.calendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arvind.assistant.screens.calendar.components.NoReadCalendarPermissionMessage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CalendarScreen(){

    val viewModel = viewModel<CalendarViewModel>()
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
        Surface(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            if(readCalendarPermissionState.hasPermission){
                Text(text = "Calendar Screen")
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