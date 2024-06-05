package com.arvind.assistant.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.arvind.assistant.Database
import com.arvind.assistant.applicationContextGlobal


fun getAndroidSqliteDriver(context: Context) = AndroidSqliteDriver(
    schema = Database.Schema,
    context = context,
    name = "app.db"
)

fun getSqliteDB(driver: SqlDriver): Database{
    return Database(
        driver = driver,

    )
}
class DBOps(
    driver: SqlDriver,
){
    val db by lazy { getSqliteDB(driver) }
    private val queries by lazy { db.appQueries }
    fun createCourse(
        courseName: String,
        requiredAttendance: Double
    ){
        queries.createCourse(
            courseName = courseName,
            requiredAttendance = requiredAttendance
        )

    }

    companion object {
        val instance: DBOps by lazy {
            DBOps(getAndroidSqliteDriver(applicationContextGlobal))
        }
    }
}