package com.esteel4u.realtimeauctionapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.service.ScheduledWorker.Companion.NOTIFICATION_ID
import com.esteel4u.realtimeauctionapp.service.ScheduledWorker.Companion.NOTIFICATION_MESSAGE
import com.esteel4u.realtimeauctionapp.service.ScheduledWorker.Companion.NOTIFICATION_TITLE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ptrbrynt.firestorelivedata.asLiveData

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val title = it.getStringExtra(NOTIFICATION_TITLE)
            val message = it.getStringExtra(NOTIFICATION_MESSAGE)
            val id = it.getStringExtra(NOTIFICATION_ID)

            // Create Notification Data
            val notificationData = Data.Builder()
                .putString(NOTIFICATION_TITLE, title)
                .putString(NOTIFICATION_MESSAGE, message)
                .putString(NOTIFICATION_ID, id)
                .build()

            // Init Worker
            val work = OneTimeWorkRequest.Builder(ScheduledWorker::class.java)
                .setInputData(notificationData)
                .build()

            val db = Firebase.firestore
            val myDocu = db.collection("products").document(message!!).asLiveData<ProductData>()
            myDocu.update( "auctionProgressStatus" , 1)

            // Start Worker
            WorkManager.getInstance().beginWith(work).enqueue()


            Log.d(javaClass.name, "WorkManager is Enqueued.")
        }
    }
}