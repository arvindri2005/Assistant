package com.arvind.assistant.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarEvent(
    val id: Long,
    val title: String,
    val description: String?,
    val start: Long,
    val end: Long,
    val location: String?,
    val allDay: Boolean = false,
    val color: Int = 0,
    val calendarId: Long,
    val recurring: Boolean = false,
    val frequency: String = "",
): Parcelable