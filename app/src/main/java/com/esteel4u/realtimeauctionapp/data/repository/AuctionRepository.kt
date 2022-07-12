package com.esteel4u.realtimeauctionapp.data.repository

import android.content.ContentValues
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.esteel4u.realtimeauctionapp.data.model.AuctionData
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.google.android.gms.common.util.DataUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.ptrbrynt.firestorelivedata.observe
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class AuctionRepository(val lifecycleOwner: LifecycleOwner) {
    private var auctioninfo = MutableLiveData<AuctionData>()
    private var auth = Firebase.auth
    private val db = Firebase.firestore

    fun setBid(price: Int, prdId: String){
        val myDocu = db.collection("auctions").document(prdId!!).asLiveData<AuctionData>()
        val task = myDocu.update(mapOf(Pair("buyUserId", auth.uid!!), Pair("bidPrice", price)))
        task.observe(lifecycleOwner, Observer { taskResult ->

            Log.d(ContentValues.TAG, "aa      " + prdId)

        })
    }

    fun getAuctionInfo(prdId: String): MutableLiveData<AuctionData> {
        val myQuery = db.collection("auctions").document(prdId).asLiveData<AuctionData>()
        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<AuctionData> ->
            if(resource.data !== null) {
                auctioninfo.postValue(resource.data!!)
            }
        })
        return auctioninfo
    }
}