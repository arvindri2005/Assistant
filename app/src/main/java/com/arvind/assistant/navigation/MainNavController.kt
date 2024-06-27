package com.arvind.assistant.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.arvind.assistant.db.DBOps
import com.arvind.assistant.screens.attendanceRecord.AttendanceRecordScreen
import com.arvind.assistant.screens.courseDetails.CourseDetailsScreen
import com.arvind.assistant.screens.createAssignment.CreateAssignmentScreen
import com.arvind.assistant.screens.main.MainScreen
import com.arvind.assistant.utils.Constants
import javax.inject.Inject

@Composable
fun MainNavController(
    mainNavHost: NavHostController,
    dbOps: DBOps
) {


    NavHost(
        navController = mainNavHost,
        startDestination = Screen.Main.route
    ){
        composable(Screen.Main.route){
            MainScreen(
                mainNavController = mainNavHost,
                dbOps = dbOps
            )
        }

        composable(Screen.CourseDetails.route){backStackEntry ->
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
                        classes = classes,
                        scheduleToBeDeleted = {schedule->
                            dbOps.deleteSchedule(schedule.scheduleId!!)
                        },
                        onAddScheduleClass = { schedule ->
                            dbOps.addScheduleClass(
                                courseId = course.courseId,
                                schedule = schedule
                            )
                        },
                        goToAttendanceRecordScreen = {
                            mainNavHost.navigate(Screen.AttendanceRecord.route.replace("{${Constants.COURSE_ID_ARG}}", courseId))
                        },
                        onExtraClassCreated = {extraClassTimings ->
                            dbOps.createExtraClass(course.courseId, extraClassTimings)
                        },
                        goToCreateAssignmentScreen = {
                            mainNavHost.navigate(Screen.CreateAssignment.route.replace("{${Constants.COURSE_ID_ARG}}", courseId))
                        }
                    )
                }
            }
        }

        composable(Screen.AttendanceRecord.route){backStackEntry ->
            val courseId = backStackEntry.arguments?.getString(Constants.COURSE_ID_ARG)
            if(courseId!=null){
                CompositionLocalProvider(
                    androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current
                ) {
                    val course = dbOps.getCourseDetailsWithId(courseId).collectAsStateWithLifecycle(initialValue = null).value
                    if(course!=null){
                        AttendanceRecordScreen(
                            course = course,
                            records = dbOps.getAttendanceRecordsForCourse(courseId.toLong())
                                .collectAsStateWithLifecycle(initialValue = listOf()).value
                        )
                    }
                    else{
                        Log.d("BottomNavController", "Course is null")
                    }
                }
            }

        }

        composable(Screen.CreateAssignment.route){backStackEntry ->
            val courseId = backStackEntry.arguments?.getString(Constants.COURSE_ID_ARG)
            CreateAssignmentScreen()
        }
    }

}