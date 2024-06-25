package com.arvind.assistant.alarmManager

import java.time.LocalDateTime
import java.time.LocalTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)