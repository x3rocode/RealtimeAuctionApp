package com.esteel4u.realtimeauctionapp.service

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ptrbrynt.firestorelivedata.asLiveData

class AlarmService: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent != null){
            Log.d(ContentValues.TAG, "reseveeveveeeeeeeeeeeeeeeeeeeeeee  " )
            val db = Firebase.firestore
            val title = intent.getStringExtra(ScheduledWorker.NOTIFICATION_TITLE)
            val message = intent.getStringExtra(ScheduledWorker.NOTIFICATION_MESSAGE)
            val myDocu = db.collection("products").document(message!!).asLiveData<ProductData>()
            if(title == "s")myDocu.update( "auctionProgressStatus" , 1) else myDocu.update( "auctionProgressStatus" , 3)

        }
    }
}