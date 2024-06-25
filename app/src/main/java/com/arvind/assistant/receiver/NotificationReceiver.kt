package com.arvind.assistant.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.arvind.assistant.applicationContextGlobal
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.db.DBOps
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver: BroadcastReceiver() {

    @Inject
    lateinit var dbOps: DBOps
    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    override fun onReceive(context: Context, intent: Intent?) {

        val message = intent?.getStringExtra("message")
        val courseId = intent?.getLongExtra("courseId", 23L)
        val classStatus = when(intent?.action){
            "Present" -> CourseClassStatus.Present
            "Absent" -> CourseClassStatus.Absent
            "Cancelled" -> CourseClassStatus.Cancelled
            else -> CourseClassStatus.Unset
        }

        if(message != null){
            Toast.makeText(context, "$message course id: $courseId", Toast.LENGTH_SHORT).show()

            CoroutineScope(Dispatchers.IO).launch {
                dbOps.getCourseDetailsWithId(courseId!!.toString()).first().let {
                    Log.d("NotificationReceiver", "Course details: $it")
                }
                val attendanceId = dbOps.getScheduleIdAndAttendanceIdForTodayByCourseId(courseId).first().attendanceId
                val scheduleId = dbOps.getScheduleIdAndAttendanceIdForTodayByCourseId(courseId).first().scheduleId
                dbOps.markAttendanceForScheduleClass(
                    attendanceId = attendanceId,
                    classStatus = classStatus,
                    scheduleId = scheduleId,
                    date = LocalDate.now(),
                    courseId = courseId
                )
            }
            notificationManager.cancel(1)

        }
    }
}