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
import com.arvind.assistant.screens.calendar.CalendarScreen
import com.arvind.assistant.screens.courseDetails.CourseDetailsScreen
import com.arvind.assistant.screens.todaySchedule.TodayScheduleScreen

@Composable
fun BottomNavController(navController: NavHostController) {

    val dbOps: DBOps = DBOps.instance
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
        navController = navController,
        startDestination = "Home"
    ){
        composable("Home"){
//            HomeScreen {
//                navController.navigate("CreateCourse")
//            }
            CreateCourseScreen(
                createCourse = { courseName, requiredAttendance, scheduleClasses ->
                    dbOps.createCourse(courseName, requiredAttendance, scheduleClasses)
                    navController.popBackStack()
                }
            )
        }
        composable("Search"){
            CalendarScreen()
        }
        composable("Profile"){
//            ProfileScreen()
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
        composable("Settings") {
            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current
            ) {
                MyCoursesScreen(
                    courses = dbOps.getAllCourses()
                        .collectAsStateWithLifecycle(initialValue = listOf()).value,
                ){courseId ->
                    navController.navigate("courseDetails/$courseId")
                }
            }

        }

        composable("courseDetails/{courseId}"){backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")

            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current
            ) {
                val classes = dbOps.getScheduledClassesForCourse(courseId!!.toLong())
                    .collectAsStateWithLifecycle(initialValue = listOf()).value
                val course = dbOps.getCourseDetailsWithId(courseId).collectAsStateWithLifecycle(initialValue = null).value
                if (course != null) {
                    CourseDetailsScreen(
                        course = course,
                        classes = classes
                    )
                }
            }
        }

        composable("CreateCourse"){
//            CreateCourseScreen(
//                createCourse = { courseName, requiredAttendance ->
//                    dbOps.createCourse(courseName, requiredAttendance)
//                    navController.popBackStack()
//                }
//            )
        }

        composable("MyCourses"){
            MyCoursesScreen(
                courses = dbOps.getAllCourses()
                    .collectAsStateWithLifecycle(initialValue = listOf()).value,
            ){

            }
        }

        composable("todaySchedule"){

        }
    }
}
