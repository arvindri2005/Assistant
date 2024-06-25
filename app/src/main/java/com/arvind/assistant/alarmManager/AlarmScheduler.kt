package com.arvind.assistant.alarmManager

interface AlarmScheduler {

    fun schedule(item: AlarmItem)

    fun cancel(item: AlarmItem)
}