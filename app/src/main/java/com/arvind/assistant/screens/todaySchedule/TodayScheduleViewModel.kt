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
class TodayScheduleViewModel @Inject constructor(): ViewModel() {

    private val alarmScheduler = AndroidAlarmScheduler(applicationContextGlobal)

    fun setAlarm(courseId: Long, courseName: String){
        val alarmItem= AlarmItem(
            time = LocalDateTime.now().plusSeconds(20),
            courseId = courseId,
            courseName = "Course Name"
        )
        alarmScheduler.schedule(alarmItem)
    }

}