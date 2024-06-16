package com.arvind.assistant.useCases.calendar

import com.arvind.assistant.domain.model.CalendarEvent
import com.arvind.assistant.domain.repository.CalendarRepository
import com.arvind.assistant.utils.formatDateForMapping

class GetAllEventsUseCase (
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(excluded: List<Int>, fromWidget: Boolean = false): Map<String, List<CalendarEvent>> {
        val events = try {
            calendarRepository.getEvents()
                .filter { it.calendarId.toInt() !in excluded }
        } catch (e: Exception) {
            return emptyMap()
        }
        return if (fromWidget)
            events.take(30).groupBy { event ->
                event.start.formatDateForMapping()
            }
        else
            events.groupBy { event ->
                event.start.formatDateForMapping()
            }

    }
}