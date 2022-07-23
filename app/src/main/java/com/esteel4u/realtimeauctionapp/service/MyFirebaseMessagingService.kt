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

            if (!isTimeAutomatic(applicationContext)) {
                Log.d(TAG, "`Automatic Date and Time` is not enabled")
                return
            }

            val isScheduled = remoteMessage.data["isScheduled"]?.toBoolean()
            isScheduled?.let {
                if (it) {
                    // This is Scheduled Notification, Schedule it
                    val scheduledTime = remoteMessage.data["scheduledTime"]
                    scheduleAlarm(scheduledTime, title, message)
                    Log.d(TAG, "앵애ㅐㅐㅐdhodhodhodh12121212o")
                } else {
                    // This is not scheduled notification, show it now
                    Log.d(TAG, "앵애ㅐㅐㅐdhodhodhodho")
                    showNotification(title!!, message!!)
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
                PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
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

    private fun showNotification(title: String, message: String) {
        NotificationUtil(applicationContext).showNotification(title, message)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}