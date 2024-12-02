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
import com.arvind.assistant.screens.courseEdit.CourseEditScreen
import com.arvind.assistant.screens.createCourse.CreateCourseScreen
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
                        onGoBack = {
                            mainNavHost.popBackStack()
                        },
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
                        onDeleteCourse = { courseId ->
                            dbOps.deleteCourse(courseId)
                        },
                        goToCourseEditScreen = {
                            mainNavHost.navigate(Screen.CourseEdit.route.replace("{${Constants.COURSE_ID_ARG}}", courseId))
                        }
                    )
                }
            }
        }

        composable(Screen.CourseEdit.route){backStackEntry->
            val courseId = backStackEntry.arguments?.getString("courseId")
            if(courseId!=null){
                CompositionLocalProvider(
                    androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current
                ) {
                    val course = dbOps.getCourseDetailsWithId(courseId).collectAsStateWithLifecycle(initialValue = null).value
                    if(course!=null){
                        CompositionLocalProvider(
                            androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current
                        ) {
                            CourseEditScreen(
                                course = course,
                                onEditCourse = {courseName, requiredAttendance ->
                                    dbOps.updateCourseDetails(
                                        id = courseId.toLong(),
                                        name = courseName,
                                        requiredAttendancePercentage = requiredAttendance,
                                    )},
                                onGoBack = {
                                    mainNavHost.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }

        composable(Screen.AttendanceRecord.route){backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
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

        composable(Screen.Add.route){
            CreateCourseScreen(
                createCourse = { courseName, requiredAttendance, scheduleClasses ->
                    val courseId = dbOps.createCourse(courseName, requiredAttendance, scheduleClasses)
                    scheduleClasses.forEach{ scheduleClass ->
                        setAlarm(courseId, courseName, scheduleClass.endTime, scheduleClass.dayOfWeek)
                    }
                    mainNavHost.popBackStack()
                }
            )
        }
    }

}