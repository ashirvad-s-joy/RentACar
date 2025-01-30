package com.inte.assignment.service


import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.inte.assignment.data.Warning
import com.inte.assignment.repository.CustomerSpeedLimitRepository
import com.inte.assignment.repository.SpeedLimitCallback
import com.inte.assignment.utils.Constants
import com.inte.assignment.utils.NotificationHandler
import kotlin.random.Random


class SpeedTrackerService : Service() {

    private lateinit var runnable: Runnable
    private val TAG: String = SpeedTrackerService::class.java.simpleName
    private var currentSpeed: Float = 0f
    private lateinit var handler: Handler
    private val CHANNEL_ID = "SpeedTrackerServiceChannel"
    private lateinit var mCarPropertyManager: CarPropertyManager

    /**
     *  CarPropertyManager call back
     */
    private val mCallBack = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
            Log.d(TAG, "onChangeEvent: $carPropertyValue")
            if (carPropertyValue.propertyId == VehiclePropertyIds.PERF_VEHICLE_SPEED) {
                val value = carPropertyValue.value as Float
                currentSpeed = value
                if (value > speedLimit) {
                    sendSpeedAlert(value)
                }
                // Here you can send a broadcast or use another method to update the UI
                Log.d(tag, "Current Speed: $value Km/hr")

               // Log.e(TAG, "Speed: is this ${Math.round(value)}")
            }
        }

        override fun onErrorEvent(errorCode: Int, propertyId: Int) {
            Log.e(TAG, "onErrorEvent: $errorCode")
        }
    }


    val repository: CustomerSpeedLimitRepository = CustomerSpeedLimitRepository()



    private val tag = "SpeedTrackerService"
    private lateinit var mCar: Car
    private var speedLimit: Float = 50f // Default speed limit

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForegroundService()
        handler = Handler()
        repository.getCustomerSpeedLimit(Constants.UserName, object : SpeedLimitCallback {
            override fun onSpeedLimitFetched(speed: Float) {
                speedLimit = speed
            }

        })!!


        //simulateSpeed()
        carPropertyInit()
//        val propertyList = mCarPropertyManager.getPropertyList()
//        Log.d(TAG, "Available speedLimit: ${speedLimit}")
//        for (property in propertyList) {
//            Log.d(TAG, "Available Property: ${property.propertyId}")
//        }
    }

    private fun carPropertyInit() {
        mCar = Car.createCar(this)
        if (mCar.isConnected) {
            mCarPropertyManager = mCar.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
            mCarPropertyManager.registerCallback(
                mCallBack,
                VehiclePropertyIds.PERF_VEHICLE_SPEED,
                CarPropertyManager.SENSOR_RATE_NORMAL
            )
        } else {
            Log.e(TAG, "Car is not connected")
        }

    }

    private fun showNotification() {

        val builder: Notification.Builder =
            NotificationHandler.setBuilder(this, currentSpeed.toInt())
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, builder.build()) // 1 is the notification ID
    }

    /**
     * simulateSpeed()
     *
     */

    private fun simulateSpeed() {
        runnable = object : Runnable {
            override fun run() {
                // Generate a random speed between 20 and 120
                currentSpeed = Random.nextFloat() * (120 - 20) + 20
                Log.d(tag, "Current Speed: $currentSpeed Km/hr")

                // Check if the speed exceeds the limit
                if (currentSpeed > speedLimit) {
                    sendSpeedAlert(currentSpeed)
                }

                // Repeat this runnable code block again every 1000 milliseconds
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)

    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        mCarPropertyManager.unregisterCallback(mCallBack)
        mCar.disconnect()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Speed Tracker Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationChannel = NotificationChannel(
                "MY_CHANNEL_ID",
                "Speed Tracker Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            manager.createNotificationChannel(notificationChannel)

        }
    }

    private fun startForegroundService() {
        val notification =
            NotificationHandler.setBuilderForService(this, currentSpeed.toInt()).build()
        startForeground(1, notification) // 1 is the notification ID
    }


    private fun sendSpeedAlert(currentSpeed: Float) {
        sendFirebaseNotification(currentSpeed)
        showNotification()
    }

    private fun sendFirebaseNotification(currentSpeed: Float) {
        println("Sending Firebase Notification: Speed exceeded: $currentSpeed km/h")
       // repository.setWarning(Warning(currentSpeed, Constants.UserName))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
