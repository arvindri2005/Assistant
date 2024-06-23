package com.arvind.assistant.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val message = intent?.getStringExtra("message")
//        val courseId = intent?.getStringExtra("courseId")!!.toLong()
        Log.d("NotificationReceiver", "onReceive: $message")

        if(message != null){
            val notificationManager = NotificationManagerCompat.from(context!!)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            notificationManager.cancel(1)
        }
//        val notificationManager = NotificationManagerCompat.from(context!!)
//        Toast.makeText(context, courseId.toString(), Toast.LENGTH_SHORT).show()
//        notificationManager.cancel(1)
    }
}