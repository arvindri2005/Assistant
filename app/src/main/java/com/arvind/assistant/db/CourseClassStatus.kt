package com.arvind.assistant.db

enum class CourseClassStatus {
    Present,
    Absent,
    Cancelled,
    Unset;

    override fun toString() = when (this) {
        Present -> "Present"
        Absent -> "Absent"
        Cancelled -> "Cancelled"
        Unset -> "Unset"

    }

    companion object {
        fun fromString(str: String) = when (str) {
            "Present" -> Present
            "Absent" -> Absent
            "Cancelled" -> Cancelled
            "Unset" -> Unset
            else -> throw IllegalArgumentException("Status can only be either one of these: Present, Absent, Cancelled, Unset")
        }
    }
}