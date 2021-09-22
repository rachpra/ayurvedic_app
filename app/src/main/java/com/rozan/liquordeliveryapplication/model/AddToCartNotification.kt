package com.rozan.liquordeliveryapplication.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class AddToCartNotification(val context: Context) {
    val CHHANEL1:String="Channel1"
    val CHHANEL2:String="Channel2"

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel1= NotificationChannel(CHHANEL1,"Chhanel1", NotificationManager.IMPORTANCE_HIGH)
            channel1.description="Added to Cart"

            val channel2= NotificationChannel(CHHANEL2,"Chhanel2", NotificationManager.IMPORTANCE_LOW)
            channel2.description="Channel2"

            val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }
}