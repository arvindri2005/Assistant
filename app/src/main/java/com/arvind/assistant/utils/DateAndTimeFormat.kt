package com.arvind.assistant.utils

import android.provider.Settings.Global.getString
import android.text.format.DateFormat
import com.arvind.assistant.R
import com.arvind.assistant.applicationContextGlobal
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


val timeFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofPattern("hh:mm a")

val dateFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofPattern("dd MMMM yyyy")

fun Long.formatDateForMapping(): String {
    val sdf = SimpleDateFormat("EEEE d, MMM yyy", Locale.getDefault())
    return sdf.format(this)
}

fun Long.monthName(): String {
    val sdf = if (this.isCurrentYear())
        SimpleDateFormat("MMMM", Locale.getDefault())
    else
        SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return sdf.format(this)
}

fun Long.isCurrentYear(): Boolean {
    val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
    return sdf.format(this) == sdf.format(Date())
}


fun formatEventStartEnd(start: Long, end: Long, location: String?, allDay: Boolean): String {
    return if (allDay)
        "All Day"
    else
        applicationContextGlobal.getString(
            if (!location.isNullOrBlank())
                R.string.event_time_at
            else R.string.event_time,
            start.formatTime(),
            end.formatTime(),
            location ?: ""
        )
}

fun is24Hour() = DateFormat.is24HourFormat(applicationContextGlobal)


fun Long.formatTime(): String {
    val is24 = is24Hour()
    val sdf = SimpleDateFormat(if (is24) "H:mm" else "h:mm a", Locale.getDefault())
    val sdfNoMinutes = SimpleDateFormat(if (is24) "H:mm" else "h a", Locale.getDefault())
    val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(this)
    return if (minutes == "00") sdfNoMinutes.format(this) else sdf.format(this)
}