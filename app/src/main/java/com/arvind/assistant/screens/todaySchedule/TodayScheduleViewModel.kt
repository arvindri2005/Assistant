package com.arvind.assistant.screens.todaySchedule

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.arvind.assistant.applicationContextGlobal
import com.arvind.assistant.db.DBOps
import com.arvind.assistant.receiver.NotificationReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodayScheduleViewModel @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
): ViewModel() {

    fun showSimpleNotification(courseName: String, courseId: Long){

        val intent1 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Present in $courseName")
            putExtra("courseId", courseId.toString())
        }
        val pendingIntent1 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            0,
            intent1,
            PendingIntent.FLAG_IMMUTABLE
        )
        val intent2 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Absent in $courseName")
        }
        val pendingIntent2 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            1,
            intent2,
            PendingIntent.FLAG_IMMUTABLE
        )
        val intent3 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Cancel in $courseName")
        }
        val pendingIntent3 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            2,
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
            .setContentTitle(courseName)
            .addAction(0, "Present", pendingIntent1)
            .addAction(1, "Absent", pendingIntent2)
            .addAction(2, "Cancel", pendingIntent3)
        notificationManager.notify(1, notificationBuilder.build())
    }

}