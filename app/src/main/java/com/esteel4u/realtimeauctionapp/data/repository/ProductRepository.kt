package com.esteel4u.realtimeauctionapp.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build.ID
import android.provider.ContactsContract.DisplayNameSources.NICKNAME
import android.provider.SimPhonebookContract.SimRecords.PHONE_NUMBER
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
import com.ptrbrynt.firestorelivedata.asLiveData
import com.ptrbrynt.firestorelivedata.observe

class ProductRepository() {

    private var productList = MutableLiveData<List<ProductData>>()

    private var auth = Firebase.auth
    private val db = Firebase.firestore


//    fun getAllPrdData(): MutableLiveData<List<ProductData>> {
//        val myCollection = db.collection("products").asLiveData<ProductData>()
//        myCollection.observe(HomeFragment.instance.viewLifecycleOwnerLiveData, Observer{
//            productList.postValue(it.data!!)
//        })
//        return productList
//    }

}