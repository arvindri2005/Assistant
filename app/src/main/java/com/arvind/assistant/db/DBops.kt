package com.arvind.assistant.db

import android.content.Context
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.arvind.assistant.Attendance
import com.arvind.assistant.CourseSchedule
import com.arvind.assistant.Database
import com.arvind.assistant.ExtraClasses
import com.arvind.assistant.applicationContextGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime


fun getAndroidSqliteDriver(context: Context) = AndroidSqliteDriver(
    schema = Database.Schema,
    context = context,
    name = "app.db"
)

fun getSqliteDB(driver: SqlDriver): Database{
    val enumAdapter = EnumColumnAdapter<CourseClassStatus>()
    return Database(
        driver = driver,
        AttendanceAdapter = Attendance.Adapter(
            classStatusAdapter = enumAdapter,
            dateAdapter = LocalDateAdapter
        ),

        CourseScheduleAdapter = CourseSchedule.Adapter(
            weekDayAdapter = DayOfWeekAdapter,
            startTimeAdapter = LocalTimeAdapter,
            endTimeAdapter = LocalTimeAdapter
        ),
        ExtraClassesAdapter = ExtraClasses.Adapter(
            dateAdapter = LocalDateAdapter,
            startTimeAdapter = LocalTimeAdapter,
            endTimeAdapter = LocalTimeAdapter,
            classStatusAdapter = enumAdapter
        )


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
            mapper = { courseId, courseName, requiredAttendance, _, presents, absents, cancels, unsets ->


                CourseDetails(
                    courseId = courseId,
                    courseName = courseName,
                    requiredAttendance = requiredAttendance,
                    currentAttendancePercentage = if (presents + absents == 0L) 100.0 else (presents.toDouble() / (presents + absents)) * 100,
                    presents = presents.toInt(),
                    absents = absents.toInt(),
                    cancels = cancels.toInt(),
                    unsets = unsets.toInt()
                )
            }
        ).asFlow().mapToList(Dispatchers.IO)
    }


    fun getScheduleAndExtraClassesForToday():Flow<List<Pair<AttendanceRecordHybrid, AttendanceCounts>>>{
        val scheduleClassesFlow: Flow<List<AttendanceRecordHybrid>> = queries.getCourseListForToday(
            mapper = { attendanceId, scheduleId, courseId, courseName, startTime, endTime, classStatus, date ->
                AttendanceRecordHybrid.ScheduledClass(
                    attendanceId = attendanceId,
                    scheduleId = scheduleId,
                    startTime = startTime,
                    endTime = endTime,
                    courseName = courseName,
                    date = date ?: LocalDate.now(),
                    classStatus = CourseClassStatus.fromString(classStatus),
                    courseId = courseId
                )
            }
        ).asFlow().mapToList(Dispatchers.IO)
        val extraClassesFlow: Flow<List<AttendanceRecordHybrid>> =
            queries.getExtraClassesListForToday(mapper = { courseId, courseName, startTime, endTime, classStatus, extraClassId, date ->
                AttendanceRecordHybrid.ExtraClass(
                    extraClassId = extraClassId,
                    startTime = startTime,
                    endTime = endTime,
                    courseName = courseName,
                    date = date,
                    classStatus = classStatus,
                    courseId = courseId
                )
            }).asFlow().mapToList(Dispatchers.IO)
        return scheduleClassesFlow.combine(extraClassesFlow) { list1, list2 ->
            (list1 + list2).sortedByDescending { it.startTime }
        }.map { attendanceRecords ->
            attendanceRecords.map {
                Pair(it, AttendanceCounts(
                    present = 100,
                    absents = 0,
                    percent = 100.0,
                    cancels = 0,
                    unsets = 0L,
                    requiredPercentage = 75.0
                ) )
            }
        }
    }

    fun markAttendanceForScheduleClass(
        attendanceId: Long?,
        classStatus: CourseClassStatus,
        scheduleId: Long?,
        date: LocalDate,
        courseId: Long
    ) {
        if (attendanceId != null)
            queries.markAttendance(attendanceId, classStatus, scheduleId, date, courseId)
        else queries.markAttendanceInsert(classStatus, scheduleId, date, courseId)
    }


    fun markAttendanceForExtraClass(
        extraClassId: Long,
        status: CourseClassStatus
    ) = queries.updateExtraClassStatus(extraClassId = extraClassId, status = status)



    companion object {
        val instance: DBOps by lazy {
            DBOps(getAndroidSqliteDriver(applicationContextGlobal))
        }
    }
}