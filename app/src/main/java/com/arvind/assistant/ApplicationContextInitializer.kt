package com.arvind.assistant

import android.content.Context
import androidx.startup.Initializer

private var appContext: Context? = null
val applicationContextGlobal
    get() = appContext!!

internal class ApplicationContextInitializer : Initializer<Context> {

    override fun create(context: Context): Context {
        context.applicationContext.also { appContext = it }
        return context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
