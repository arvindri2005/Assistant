package com.arvind.assistant.screens.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.arvind.assistant.domain.model.Calendar
import com.arvind.assistant.domain.model.CalendarEvent

class CalendarViewModel(): ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set
}

data class UiState(
    val events: Map<String, List<CalendarEvent>> = emptyMap(),
    val calendars: Map<String, List<Calendar>> = emptyMap(),
    val calendarsList: List<Calendar> = emptyList(),
    val excludedCalendars: MutableList<Int> = mutableListOf(),
    val months: List<String> = emptyList(),
    val navigateUp: Boolean = false,
    val error: String? = null
)