package com.arvind.assistant.domain.repository

import com.arvind.assistant.domain.model.Calendar
import com.arvind.assistant.domain.model.CalendarEvent


interface CalendarRepository {

    suspend fun getEvents(): List<CalendarEvent>

    suspend fun getCalendars(): List<Calendar>

    suspend fun addEvent(event: CalendarEvent)

    suspend fun deleteEvent(event: CalendarEvent)

    suspend fun updateEvent(event: CalendarEvent)

    suspend fun createCalendar()
}