package com.arvind.assistant.navigation

import com.arvind.assistant.utils.Constants

sealed class Screen(val route: String) {
    data object Main : Screen("main_screen")
    data object Add : Screen("add_course_screen")
    data object Calendar : Screen("calendar_screen")
    data object Today : Screen("today_schedule_screen")
    data object Courses : Screen("courses_screen")
    data object CourseDetails : Screen("course_details_screen/{${Constants.COURSE_ID_ARG}}")
    data object AttendanceRecord : Screen("attendanceRecord/{${Constants.COURSE_ID_ARG}}")
    data object CreateAssignment : Screen("create_assignment_screen/{${Constants.COURSE_ID_ARG}}")
}