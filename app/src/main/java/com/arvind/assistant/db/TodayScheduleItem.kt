package com.arvind.assistant.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

sealed  interface AttendanceRecordHybrid : Parcelable {
    val courseName: String
    val courseId: Long
    val startTime: LocalTime
    val endTime: LocalTime
    val classStatus: CourseClassStatus
    val date: LocalDate

    @Parcelize
    class ScheduledClass(
        val attendanceId: Long?,
        val scheduleId: Long?,
        override val courseId: Long,
        override val courseName: String,
        override val startTime: LocalTime,
        override val endTime: LocalTime,
        override val classStatus: CourseClassStatus,
        override val date: LocalDate,
    ) : AttendanceRecordHybrid

    @Parcelize
    class ExtraClass(
        val extraClassId: Long,
        override val courseId: Long,
        override val courseName: String,
        override val startTime: LocalTime,
        override val endTime: LocalTime,
        override val classStatus: CourseClassStatus,
        override val date: LocalDate,
    ) : AttendanceRecordHybrid
}