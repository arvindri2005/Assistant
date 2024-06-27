package com.arvind.assistant.receiver

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arvind.assistant.applicationContextGlobal
import com.arvind.assistant.db.AttendanceRecordHybrid
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder
    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    override fun onReceive(context: Context?, intent: Intent?) {
        val courseId = intent?.getLongExtra("courseId", 0)
        val courseName = intent?.getStringExtra("courseName")

        if (courseName != null && courseId != null) {
            showSimpleNotification(courseName, courseId)
        }
    }


    private fun showSimpleNotification(courseName: String, courseId: Long){

        val intent1 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Present in $courseName")
            putExtra("courseId", courseId)
            action = "Present"
        }
        val pendingIntent1 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            courseId.toInt() * 10,
            intent1,
            PendingIntent.FLAG_IMMUTABLE
        )
        val intent2 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Absent in $courseName")
            putExtra("courseId", courseId)
            action = "Absent"
        }
        val pendingIntent2 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            courseId.toInt() * 10 + 2,
            intent2,
            PendingIntent.FLAG_IMMUTABLE
        )
        val intent3 = Intent(applicationContextGlobal, NotificationReceiver::class.java).apply {
            putExtra("message", "Cancel in $courseName")
            putExtra("courseId", courseId)
            action = "Cancelled"
        }
        val pendingIntent3 = PendingIntent.getBroadcast(
            applicationContextGlobal,
            courseId.toInt() * 10 + 3,
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
        notificationManager.notify(courseId.toInt()*23, notificationBuilder.build())
    }
}