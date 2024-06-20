package com.arvind.assistant.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arvind.assistant.screens.createCourse.CreateCourseScreen
import com.arvind.assistant.screens.myCourses.MyCoursesScreen
import com.arvind.assistant.db.AttendanceRecordHybrid
import com.arvind.assistant.db.CourseClassStatus
import com.arvind.assistant.db.DBOps
import com.arvind.assistant.screens.attendanceRecord.AttendanceRecordScreen
import com.arvind.assistant.screens.calendar.CalendarScreen
import com.arvind.assistant.screens.courseDetails.CourseDetailsScreen
import com.arvind.assistant.screens.todaySchedule.TodayScheduleScreen
import com.arvind.assistant.utils.Constants

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
        startDestination = Screen.Add.route
    ){
        composable(Screen.Add.route){
            CreateCourseScreen(
                createCourse = { courseName, requiredAttendance, scheduleClasses ->
                    dbOps.createCourse(courseName, requiredAttendance, scheduleClasses)
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
