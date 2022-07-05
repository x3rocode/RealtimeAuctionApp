package com.esteel4u.realtimeauctionapp.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build.ID
import android.provider.ContactsContract.DisplayNameSources.NICKNAME
import android.provider.SimPhonebookContract.SimRecords.PHONE_NUMBER
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.esteel4u.realtimeauctionapp.view.ui.fragments.HomeFragment
import com.google.api.ResourceProto.resource
import com.google.firebase.analytics.FirebaseAnalytics.Event.SIGN_UP
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.TaskStatus
import com.ptrbrynt.firestorelivedata.asLiveData
import com.ptrbrynt.firestorelivedata.observe
import java.util.*

class ProductRepository(val lifecycleOwner: LifecycleOwner) {

    private var productList = MutableLiveData<List<ProductData>>()
    private val db = Firebase.firestore


    fun getAllPrdList(): MutableLiveData<List<ProductData>> {
        val myQuery = db.collection("products").orderBy("auctionProgressStatus") .asLiveData<ProductData>()
        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<List<ProductData>> ->
            if(resource.data !== null) {

                productList.postValue(resource.data!!)
            }
        })
        return productList
    }

    fun getTodayAuctionList(): MutableLiveData<List<ProductData>> {
        val myQuery = db.collection("products").orderBy("auctionProgressStatus") .asLiveData<ProductData>()
        var status = 0
        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<List<ProductData>> ->
            if(resource.data !== null) {
                resource.data?.forEach {

                    val myDocu = db.collection("products").document(it.prdId!!).asLiveData<ProductData>()

                    //대기
                    if (DateUtils.isToday(it.startDate!!.toDate().time )
                        && it.startDate!!.toDate().after(Date())) {

                        status = 1

                    } //진행중
                    else if (it.startDate!!.toDate().before(Date())
                        && it.endDate!!.toDate().after(Date())) {
                        status = 2
                        //완료
                    } else if (DateUtils.isToday(it.endDate!!.toDate().time)
                        && it.endDate!!.toDate().before(Date())) {
                        status = 3
                    }

                    val task = myDocu.update( "auctionProgressStatus" , status)
                    task.observe(lifecycleOwner, Observer { taskResult ->

                        Log.d(ContentValues.TAG, "aa " + it.prdName)
                        Log.d(ContentValues.TAG, "1111 " +taskResult.data)
                        Log.d(ContentValues.TAG, "1111222 " +taskResult.exception)
                        Log.d(ContentValues.TAG, "111331 " +taskResult.status)
                    })

                }
            }
        })
        return productList
    }

    fun updateAuctionStatus(status: Int, prdId: String){
//        val myDocu = db.collection("products").document(prdId!!).asLiveData<ProductData>()
//        myDocu.update("auctionProgressStatus", status)


    }

}