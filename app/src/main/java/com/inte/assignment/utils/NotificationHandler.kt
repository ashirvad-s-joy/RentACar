package com.inte.assignment.utils

import android.R
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.inte.assignment.MainActivity


object NotificationHandler {

    private val CHANNEL_ID = "SpeedTrackerServiceChannel"


    fun setBuilder(context: Context,speed : Int): Notification.Builder{
        val notificationIntent = Intent(
            context,
            MainActivity::class.java
        )
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )
        return Notification.Builder(context, "MY_CHANNEL_ID")
            .setContentTitle("Your Speed Limit Exceed")
            .setContentText("you were driving at ${speed}.")
            .setSmallIcon(R.drawable.ic_menu_manage) // Replace with your own icon
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

    }

    fun setBuilderForService(context: Context,speed : Int): Notification.Builder{
        val notificationIntent = Intent(
            context,
            MainActivity::class.java
        )
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )
        return Notification.Builder(context, "SpeedTrackerServiceChannel")
            .setContentTitle("Your Service is Running")
            .setContentText("Speed is being monitored by your rental company")
            .setSmallIcon(R.drawable.ic_menu_manage) // Replace with your own icon
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

    }


}