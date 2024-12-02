package com.arvind.assistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arvind.assistant.alarmManager.AlarmItem
import com.arvind.assistant.alarmManager.AndroidAlarmScheduler
import com.arvind.assistant.applicationContextGlobal
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.db.DBOps
import com.arvind.assistant.screens.calendar.CalendarScreen
import com.arvind.assistant.screens.createCourse.CreateCourseScreen
import com.arvind.assistant.screens.myCourses.MyCoursesScreen
import com.arvind.assistant.screens.todaySchedule.TodayScheduleScreen
import com.arvind.assistant.utils.Constants
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun BottomNavController(
    bottomNavController: NavHostController,
    mainNavController: NavHostController,
    dbOps: DBOps
) {


    val onSetClassStatus = remember{
        { item: AttendanceRecordHybrid, status: CourseClassStatus ->
            when (item) {
                is AttendanceRecordHybrid.ExtraClass -> {
                    dbOps.markAttendanceForExtraClass(
                        item.extraClassId,
                        status
                    )
                }

                is AttendanceRecordHybrid.ScheduledClass -> {
                    dbOps.markAttendanceForScheduleClass(
                        attendanceId = item.attendanceId,
                        classStatus = status,
                        scheduleId = item.scheduleId,
                        date = item.date,
                        courseId = item.courseId
                    )
                }
            }
        }
    }

    NavHost(
        navController = bottomNavController,
        startDestination = Screen.Today.route
    ){
        composable(Screen.Add.route){
            CreateCourseScreen(
                createCourse = { courseName, requiredAttendance, scheduleClasses ->
                    val courseId = dbOps.createCourse(courseName, requiredAttendance, scheduleClasses)
                    scheduleClasses.forEach{ scheduleClass ->
                        setAlarm(courseId, courseName, scheduleClass.endTime, scheduleClass.dayOfWeek)
                    }
                    bottomNavController.popBackStack()
                }
            )
        }
        composable(Screen.Calendar.route){
            CalendarScreen()
        }
        composable(Screen.Today.route){
            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current
            ) {
                TodayScheduleScreen(
                    onSetClassStatus = onSetClassStatus,
                    todaySchedule = dbOps.getScheduleAndExtraClassesForToday()
                        .collectAsStateWithLifecycle(initialValue = listOf()).value
                )
            }

        }
        composable(Screen.Courses.route) {
            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current
            ) {
                MyCoursesScreen(
                    courses = dbOps.getAllCourses()
                        .collectAsStateWithLifecycle(initialValue = listOf()).value,
                ){courseId ->
                    mainNavController.navigate(
                        Screen.CourseDetails.route.replace(
                            "{${Constants.COURSE_ID_ARG}}",
                            courseId.toString()
                        )
                    )
                }
            }
        }
    }
}

fun setAlarm(courseId: Long, courseName: String, time: LocalTime, dayOfWeek: DayOfWeek){
    val date = getNextDate(dayOfWeek)
    val dateTime = LocalDateTime.of(date, time)
    val alarmScheduler = AndroidAlarmScheduler(applicationContextGlobal)
    val alarmItem= AlarmItem(
        time = dateTime.plusSeconds(5),
        courseId = courseId,
        courseName = courseName
    )
    alarmScheduler.schedule(alarmItem)
}

fun getNextDate(dayOfWeek: DayOfWeek): LocalDate {
    var date = LocalDate.now()
    while (date.dayOfWeek != dayOfWeek) {
        date = date.plusDays(1)
    }
    return date
}
