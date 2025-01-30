package com.inte.assignment.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.inte.assignment.data.User
import com.inte.assignment.data.Warning


class CustomerSpeedLimitRepository {
    private var customerSpeedLimits: Float = 20f
    val database = FirebaseDatabase.getInstance()

    fun getCustomerSpeedLimit(customerId: String, callback: SpeedLimitCallback): Float {


        val usersRef = database.getReference("users")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(
                        User::class.java
                    )
                    if (user != null) {
                        Log.d(
                            "User Info",
                            ("Name: " + user.name).toString() + ", Age: " + user.speed
                        )

                        if (customerId == user.name) {

                            Log.w("speedLimit", "${user.speed.toFloat()}")

                            callback.onSpeedLimitFetched(user.speed.toFloat())


                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException())
            }
        })


        return customerSpeedLimits // Default speed limit is 120 km/h
    }


    fun setWarning(w: Warning) {

        val ref = database.getReference("overspeeding")

        val key = ref.push().key // Generates a unique key for the event
        if (key != null) {
            ref.child(key).setValue(w).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Overspeeding", "Data saved successfully.")
                } else {
                    Log.e("Overspeeding", "Failed to save data.", task.exception)
                }
            }
        }


    }

    // to communicate with aws service.
    fun awsService(){

    }
}

interface SpeedLimitCallback {
    fun onSpeedLimitFetched(speed: Float)
}
