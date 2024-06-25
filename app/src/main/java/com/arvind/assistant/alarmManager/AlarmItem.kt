package com.arvind.assistant.alarmManager

import java.time.LocalDateTime
import java.time.LocalTime

data class AlarmItem(
    val time: LocalDateTime,
    val courseId: Long,
    val courseName: String
)