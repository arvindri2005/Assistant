package com.arvind.assistant.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.cash.sqldelight.db.SqlDriver
import com.arvind.assistant.Database
import com.arvind.assistant.R
import com.arvind.assistant.app.dataStore
import com.arvind.assistant.data.repisotory.CalendarRepositoryImpl
import com.arvind.assistant.data.repisotory.SettingsRepositoryImpl
import com.arvind.assistant.db.DBOps
import com.arvind.assistant.db.getAndroidSqliteDriver
import com.arvind.assistant.db.getSqliteDB
import com.arvind.assistant.domain.repository.CalendarRepository
import com.arvind.assistant.domain.repository.SettingsRepository
import com.arvind.assistant.receiver.NotificationReceiver
import com.arvind.assistant.screens.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver =
        getAndroidSqliteDriver(context)

    @Singleton
    @Provides
    fun provideDatabase(driver: SqlDriver): Database = getSqliteDB(driver)

    @Singleton
    @Provides
    fun provideAppContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideCalendarRepository(@ApplicationContext context: Context): CalendarRepository =
        CalendarRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository =
        SettingsRepositoryImpl(context.dataStore)


    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {

        val clickIntent = Intent(context, MainActivity::class.java)
        val pendingClickIntent = PendingIntent.getActivity(
            context,
            0,
            clickIntent,
            PendingIntent.FLAG_MUTABLE
        )
        return NotificationCompat.Builder(context, "channelId")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingClickIntent)
            .setAutoCancel(true)
    }

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        val channel = NotificationChannel(
            "channelId",
            "Assistant",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)
        return notificationManager
    }

}