package com.arvind.assistant.utils

import com.arvind.assistant.domain.model.CalendarEvent


const val CALENDAR_FREQ_DAILY = "DAILY"
const val CALENDAR_FREQ_WEEKLY = "WEEKLY"
const val CALENDAR_FREQ_MONTHLY = "MONTHLY"
const val CALENDAR_FREQ_YEARLY = "YEARLY"
const val CALENDAR_FREQ_NEVER = "NEVER"

fun String.toUIFrequency(): String {
    return when (this) {
        CALENDAR_FREQ_DAILY -> "Every day"
        CALENDAR_FREQ_WEEKLY -> "Every Week"
        CALENDAR_FREQ_MONTHLY -> "Every Month"
        CALENDAR_FREQ_YEARLY -> "Every Year"
        else -> "Do not repeat"
    }
}

fun CalendarEvent.getEventDuration(): String {
    return "P${(end - start) / 1000}S"
}

fun String.extractEndFromDuration(start: Long): Long {
    return try {
        val duration = this.substring(1, this.length - 1).toLong() * 1000
        start + duration
    }catch (e: Exception) {
        start
    }
}

fun CalendarEvent.getEventRRule(): String {
    return buildString {
        append("FREQ=${frequency}")
        // will be implemented later
//       if (until > 0) {
//           val formattedUntil = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).format(until)
//           append(";UNTIL=${formattedUntil}")
//       }
//       if (count > 0) {
//           append(";COUNT=${count}")
//       }
//       if (interval > 0) {
//           append(";INTERVAL=${interval}")
//       }
    }
}

fun String.extractFrequency(): String {
    return if (this.contains("FREQ=")) {
        val freq = this.substringAfter("FREQ=")
        freq.substringBefore(";")
    } else CALENDAR_FREQ_NEVER
}

// will be implemented later
//fun String.extractUntil(): Long {
//    return if (this.contains("UNTIL=")) {
//        val until = this.substringAfter("UNTIL=")
//        SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).parse(until.substringBefore(";"))?.time ?: 0
//    } else 0
//}
//fun String.extractCount(): Int {
//    return if (this.contains("COUNT=")) {
//        val count = this.substringAfter("COUNT=")
//        count.substringBefore(";").toInt()
//    } else 0
//}
//fun String.extractInterval(): Int {
//    return if (this.contains("INTERVAL=")) {
//        val interval = this.substringAfter("INTERVAL=")
//        interval.substringBefore(";").toInt()
//    } else 0
//}