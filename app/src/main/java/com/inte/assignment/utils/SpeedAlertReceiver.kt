package com.inte.assignment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SpeedAlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val currentSpeed = intent?.getFloatExtra("currentSpeed", 0f) ?: 0f
        Log.d("SpeedAlertReceiver", "Received speed alert: $currentSpeed m/s")

        // Here you can show a notification or a dialog
        // For example, you can start an activity to show the alert
    }
}