package com.arvind.assistant.db

import android.content.Context
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.arvind.assistant.CourseSchedule
import com.arvind.assistant.Database
import com.arvind.assistant.applicationContextGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalTime


fun getAndroidSqliteDriver(context: Context) = AndroidSqliteDriver(
    schema = Database.Schema,
    context = context,
    name = "app.db"
)

fun getSqliteDB(driver: SqlDriver): Database{
    return Database(
        driver = driver,
        CourseScheduleAdapter = CourseSchedule.Adapter(
            weekDayAdapter = DayOfWeekAdapter,
            startTimeAdapter = LocalTimeAdapter,
            endTimeAdapter = LocalTimeAdapter
        ),

    )
}
class DBOps(
    driver: SqlDriver,
){
    val db by lazy { getSqliteDB(driver) }
    private val queries by lazy { db.appQueries }
    fun createCourse(
        courseName: String,
        requiredAttendance: Double,
        schedule: List<ClassScheduleDetails>
    ): Long{
        return db.transactionWithResult {
            queries.createCourse(
                courseName = courseName,
                requiredAttendance = requiredAttendance
            )
            val courseId = queries.getLastInsertRowID().executeAsOne()
            schedule.forEach { (dayOfWeek, startTime, endTime, _) ->
                queries.createCourseScheduleForCourse(
                    courseId = courseId,
                    weekDay = dayOfWeek,
                    startTime = startTime,
                    endTime = endTime,
                    includeInSchedule = 1
                )

            }
            courseId
        }


    }

    fun getAllCourses(): Flow<List<CourseDetails>>{
        return queries.getAllCourses(
            mapper = { courseId, courseName, requiredAttendance ->
                CourseDetails(
                    courseId = courseId,
                    courseName = courseName,
                    requiredAttendance = requiredAttendance
                )
            }
        ).asFlow().mapToList(Dispatchers.IO)
    }



    companion object {
        val instance: DBOps by lazy {
            DBOps(getAndroidSqliteDriver(applicationContextGlobal))
        }
    }
}