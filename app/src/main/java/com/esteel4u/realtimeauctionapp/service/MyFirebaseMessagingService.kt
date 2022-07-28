package com.esteel4u.realtimeauctionapp.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.esteel4u.realtimeauctionapp.service.ScheduledWorker.Companion.NOTIFICATION_MESSAGE
import com.esteel4u.realtimeauctionapp.service.ScheduledWorker.Companion.NOTIFICATION_TITLE
import com.esteel4u.realtimeauctionapp.utils.NotificationUtil
import com.esteel4u.realtimeauctionapp.utils.isTimeAutomatic
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a data payload.

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            if(remoteMessage.notification != null){
                Log.d(TAG, "ssssssss: ${remoteMessage.notification!!.title}")
                Log.d(TAG, "ssssssss: ${remoteMessage.notification!!.body}")
            }

            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            val tag = remoteMessage.data["tag"]

            if (!isTimeAutomatic(applicationContext)) {
                Log.d(TAG, "`Automatic Date and Time` is not enabled")
                return
            }

            when(tag){
                "start" -> {
                    val isScheduled = remoteMessage.data["isScheduled"]?.toBoolean()
                    val scheduledTime = remoteMessage.data["scheduledTime"]
                    scheduleAlarm(scheduledTime, title, message)
                }
                "end" -> {
                    val id = remoteMessage.data["buyuserid"]
                    showNotification(title!!, message!!, id!!, tag)
                }
                "loser" -> {
                    val id = remoteMessage.data["prdId"]
                    showNotification(title!!, message!!, id!!, tag)
                }
            }
        }
    }

    private fun scheduleAlarm(
        scheduledTimeString: String?,
        title: String?,
        message: String?
    ) {
        val alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =
            Intent(applicationContext, NotificationBroadcastReceiver::class.java).let { intent ->
                intent.putExtra(NOTIFICATION_TITLE, title)
                intent.putExtra(NOTIFICATION_MESSAGE, message)
                PendingIntent.getBroadcast(applicationContext, message.hashCode() , intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
            }

        val scheduledTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .parse(scheduledTimeString!!)

        scheduledTime?.let {
            if(it.after(Date()))
            alarmMgr.setExact(
                AlarmManager.RTC_WAKEUP,
                it.time,
                alarmIntent
            )
        }
    }

    private fun showNotification(title: String, message: String, id: String, tag: String) {
        NotificationUtil(applicationContext).showNotification(title, message, id, tag)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}