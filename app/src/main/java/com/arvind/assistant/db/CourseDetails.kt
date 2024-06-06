package com.arvind.assistant.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CourseDetails(
    val courseId: Long,
    val courseName: String,
    val requiredAttendance: Double
) : Parcelable
