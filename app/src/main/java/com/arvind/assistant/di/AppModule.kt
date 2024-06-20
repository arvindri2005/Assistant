package com.arvind.assistant.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.arvind.assistant.Database
import com.arvind.assistant.app.dataStore
import com.arvind.assistant.data.repisotory.CalendarRepositoryImpl
import com.arvind.assistant.data.repisotory.SettingsRepositoryImpl
import com.arvind.assistant.db.DBOps
import com.arvind.assistant.db.getAndroidSqliteDriver
import com.arvind.assistant.db.getSqliteDB
import com.arvind.assistant.domain.repository.CalendarRepository
import com.arvind.assistant.domain.repository.SettingsRepository
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


}