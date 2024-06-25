package com.arvind.assistant.screens.todaySchedule

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.arvind.assistant.alarmManager.AlarmItem
import com.arvind.assistant.alarmManager.AndroidAlarmScheduler
import com.arvind.assistant.applicationContextGlobal
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.receiver.NotificationReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject


@HiltViewModel
class TodayScheduleViewModel @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
): ViewModel() {

    private val alarmScheduler = AndroidAlarmScheduler(applicationContextGlobal)
    private var alarmItem= AlarmItem(
        time = LocalDateTime.now().plusSeconds(20),
        message = "Wake up"
    )

    fun showSimpleNotification(course: AttendanceRecordHybrid){

        alarmItem.let(alarmScheduler::schedule)

        val intent1 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Present in ${course.courseName}")
            putExtra("courseId", course.courseId)
            action = "Present"
        }
        val pendingIntent1 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            course.courseId.toInt() * 10,
            intent1,
            PendingIntent.FLAG_IMMUTABLE
        )
        val intent2 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Absent in ${course.courseName}")
            putExtra("courseId", course.courseId)
            action = "Absent"
        }
        val pendingIntent2 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            course.courseId.toInt() * 10 + 2,
            intent2,
            PendingIntent.FLAG_IMMUTABLE
        )
        val intent3 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Cancel in ${course.courseName}")
            putExtra("courseId", course.courseId)
            action = "Cancelled"
        }
        val pendingIntent3 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            course.courseId.toInt() * 10 + 3,
            intent3,
            PendingIntent.FLAG_IMMUTABLE
        )

        if (ActivityCompat.checkSelfPermission(
                applicationContextGlobal,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationBuilder
            .setContentTitle(course.courseName)
            .addAction(0, "Present", pendingIntent1)
            .addAction(1, "Absent", pendingIntent2)
            .addAction(2, "Cancel", pendingIntent3)
        notificationManager.notify(1, notificationBuilder.build())
    }

}