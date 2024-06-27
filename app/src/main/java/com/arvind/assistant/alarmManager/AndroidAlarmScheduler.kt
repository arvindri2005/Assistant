package com.arvind.assistant.alarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.arvind.assistant.receiver.AlarmReceiver
import com.arvind.assistant.utils.Constants
import java.time.ZoneId

class AndroidAlarmScheduler(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(Constants.ALARM_COURSE_ID, item.courseId)
            putExtra(Constants.ALARM_COURSE_NAME, item.courseName)
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            item.time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            AlarmManager.INTERVAL_DAY * 7,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}