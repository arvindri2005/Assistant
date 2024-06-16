package com.arvind.assistant.screens.calendar

import com.arvind.assistant.domain.model.Calendar
import com.arvind.assistant.domain.model.CalendarEvent

sealed class CalendarViewModelEvent {
    data class ReadPermissionChanged(val hasPermission: Boolean) : CalendarViewModelEvent()

}
