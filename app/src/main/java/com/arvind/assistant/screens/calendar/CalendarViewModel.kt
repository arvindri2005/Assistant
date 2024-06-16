package com.arvind.assistant.screens.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arvind.assistant.domain.model.Calendar
import com.arvind.assistant.domain.model.CalendarEvent
import com.arvind.assistant.useCases.calendar.GetAllCalendarsUseCase
import com.arvind.assistant.useCases.calendar.GetAllEventsUseCase
import com.arvind.assistant.useCases.settings.GetSettingsUseCase
import com.arvind.assistant.utils.Constants
import com.arvind.assistant.utils.monthName
import com.arvind.assistant.utils.toIntList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach




class CalendarViewModel(
    private val getAllEventsUseCase: GetAllEventsUseCase,
    private val getAllCalendarsUseCase: GetAllCalendarsUseCase,
    private val getSettings: GetSettingsUseCase
): ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    private var updateEventsJob : Job? = null

    fun onEvent(event: CalendarViewModelEvent) {
        when(event){

            is CalendarViewModelEvent.ReadPermissionChanged -> {
                if (event.hasPermission) collectSettings()
                else updateEventsJob?.cancel()
            }
        }
    }

    private fun collectSettings() {
        updateEventsJob = getSettings(
            stringSetPreferencesKey(Constants.EXCLUDED_CALENDARS_KEY),
            emptySet()
        ).onEach {calendarsSet ->
            val events = getAllEventsUseCase(calendarsSet.toIntList())
            val calendars = getAllCalendarsUseCase(calendarsSet.toIntList())
            val allCalendars = getAllCalendarsUseCase(emptyList())
            val months = events.map {
                it.value.first().start.monthName()
            }.distinct()
            uiState = uiState.copy(
                excludedCalendars = calendarsSet.map { it.toInt() }.toMutableList(),
                events = events,
                calendars = calendars,
                months = months,
                calendarsList = allCalendars.values.flatten()
            )
        }.launchIn(viewModelScope)
    }
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