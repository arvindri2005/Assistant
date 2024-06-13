package com.arvind.assistant.db

data class AttendanceCounts(
    val percent: Double,
    val present: Long,
    val absents: Long,
    val cancels: Long,
    val unsets: Long,
    val requiredPercentage: Double
)