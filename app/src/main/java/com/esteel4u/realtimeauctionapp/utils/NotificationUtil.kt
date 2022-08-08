package com.esteel4u.realtimeauctionapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.view.ui.activities.BidActivity
import com.esteel4u.realtimeauctionapp.view.ui.activities.LoginActivity
import com.esteel4u.realtimeauctionapp.view.ui.activities.MainActivity
import com.esteel4u.realtimeauctionapp.view.ui.activities.OnboardActivity

class NotificationUtil(private val context: Context) {

    fun showNotification(title: String, message: String, id: String, tag: String) {
        lateinit var  intent : Intent;

        if(tag == "start"){
            intent = Intent(context, BidActivity::class.java)
            if(id.isNotEmpty()) intent.putExtra("prddata", id)

        }else if(tag == "end"){
            intent = Intent(context, MainActivity::class.java)
            Log.d("22222222222222222", id)
            intent.putExtra("buyuserid", id)
        }else if(tag == "loser"){
            intent = Intent(context, BidActivity::class.java)
            if(id.isNotEmpty()) intent.putExtra("prddata", id)
        }
        intent.putExtra("tag", tag)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, message.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        //context.getString(R.string.channel_name)
        val channelId = message
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setColor(ContextCompat.getColor(context, android.R.color.black))
            .setSmallIcon(R.drawable.android_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }


        notificationManager.notify( message.hashCode(), notificationBuilder.build())
    }

}