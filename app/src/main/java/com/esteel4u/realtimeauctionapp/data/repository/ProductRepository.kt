package com.esteel4u.realtimeauctionapp.data.repository

import android.content.ContentValues
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import java.util.*

class ProductRepository(val lifecycleOwner: LifecycleOwner) {

    private var productAllList = MutableLiveData<List<ProductData>>()
    private var productTodayList = MutableLiveData<List<ProductData>>()
    private var productUserLikeList = MutableLiveData<List<ProductData>>()
    private var auth = Firebase.auth
    private val db = Firebase.firestore


    fun getAllPrdList(): MutableLiveData<List<ProductData>> {
        val myQuery = db.collection("products").orderBy("auctionProgressStatus") .asLiveData<ProductData>()
        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<List<ProductData>> ->
            if(resource.data !== null) {

                productAllList.postValue(resource.data!!)
            }
        })
        return productAllList
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
                productTodayList.postValue(resource.data!!)
            }
        })
        return productTodayList
    }

    fun getUserLikePrdList(): MutableLiveData<List<ProductData>> {

        val myQuery = db.collection("products").orderBy("auctionProgressStatus") .asLiveData<ProductData>()
        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<List<ProductData>> ->
            if(resource.data !== null) {
                val data: MutableList<ProductData>? = resource.data!!.toMutableList()

                resource.data?.forEach {
                    if(!it.notifyOnUserId!!.contains(auth.uid!!)){
                        data!!.remove(it)
                    }
                }
                productUserLikeList.postValue(data!!)
            }
        })
        return productUserLikeList
    }

    fun updateUserLikePrdList(isButtonActive: Boolean, productData: ProductData){
        var oldList = productData.notifyOnUserId
        var newList = oldList!!.toMutableList()


        if(isButtonActive){
            newList.remove(auth.uid)
        }else{
            newList.add(auth.uid!!)
        }

        val myDocu = db.collection("products").document(productData.prdId!!).asLiveData<ProductData>()
        myDocu.update("notifyOnUserId", newList)
    }


    fun updateAuctionStatus(status: Int, prdId: String){
//        val myDocu = db.collection("products").document(prdId!!).asLiveData<ProductData>()
//        myDocu.update("auctionProgressStatus", status)


    }

}