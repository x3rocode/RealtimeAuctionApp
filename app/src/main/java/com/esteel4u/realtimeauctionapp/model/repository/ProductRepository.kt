package com.esteel4u.realtimeauctionapp.model.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.model.data.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class ProductRepository(val lifecycleOwner: LifecycleOwner) {

    private var productAllList = MutableLiveData<List<ProductData>>()
    private var productTodayList = MutableLiveData<List<ProductData>>()
    private var productUserLikeList = MutableLiveData<List<ProductData>>()
    private var productUserPurchaseList = MutableLiveData<List<ProductData>>()
    private var productListByDate = MutableLiveData<List<ProductData>>()
    private var productPidData = MutableLiveData<ProductData>()
    private var productUserBidList = MutableLiveData<List<ProductData>>()
    private var auth = Firebase.auth
    private val db = Firebase.firestore
    private val udb = Firebase.firestore

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

        var alllist: MutableList<ProductData>

        val localDateTime: LocalDateTime =
            LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
        val todaymidnight =
            Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

        val localDateTime1: LocalDateTime =
            LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT)
        val tomorrowmidnight =
            Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant())

        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<List<ProductData>> ->
            if(resource.data !== null) {
                resource.data?.forEach {
                    //진행중
                    if (it.startDate!!.toDate().before(Date())
                        && it.endDate!!.toDate().after(Date())) {
                        it.auctionProgressStatus = 1

                    }
                    //진행예정
                    else if (it.startDate!!.toDate().after(Date())
                        && it.endDate!!.toDate().after(Date())) {
                        it.auctionProgressStatus = 2


                    }
                    //완료
                    else if (it.startDate!!.toDate().before(Date())
                        && it.endDate!!.toDate().before(Date())) {
                        it.auctionProgressStatus = 3
                    }
                    updateAuctionStatus(it.auctionProgressStatus!! , it.prdId!!)
                }

                //post
                productTodayList.postValue(resource.data!!.filterNot { productData: ProductData ->
                    (productData.startDate!!.toDate().before(todaymidnight) && productData.endDate!!.toDate().before(todaymidnight)) ||
                            (productData.startDate!!.toDate().after(tomorrowmidnight) && productData.endDate!!.toDate().after(tomorrowmidnight))
                })

                //subscribe
                resource.data?.forEach {
                    if(it.notifyOnUserId!!.contains(auth.uid!!)){
                        FirebaseMessaging.getInstance().subscribeToTopic(it.prdId!!);
                    }
                }
            }
        })
        return productTodayList
    }

    fun getPrdlistByDate(dateTime: DateTime) : MutableLiveData<List<ProductData>> {
        val format = SimpleDateFormat("yyyyMMdd")
        var a = format.format(dateTime.toDate())

        val myQuery = db.collection("products").orderBy("startDate") .asLiveData<ProductData>()
        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<List<ProductData>> ->
            if(resource.data !== null){
                productListByDate.postValue(resource.data!!.filter { productData :ProductData  ->
                    var b = format.format(productData.startDate!!.toDate())
                    a == b
                })
            }
        })
        return  productListByDate
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

    fun setBuyUser(prdId: String) {
        val myDocu = db.collection("products").document(prdId!!).asLiveData<ProductData>()
        val task = myDocu.update( "highestBuyUserId" , auth.uid!!)
    }

    fun setBidPrice(bidPrice: Int, prdId:String) {
        val myDocu = db.collection("products").document(prdId!!).asLiveData<ProductData>()
        myDocu.update( "bidPrice" , bidPrice)
    }

    fun updateUserLikePrdList(isButtonActive: Boolean, productData: ProductData){
        var oldList = productData.notifyOnUserId
        var newList = oldList!!.toMutableList()

        if(isButtonActive){
            newList.remove(auth.uid)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(productData.prdId!!);
        }else{
            newList.add(auth.uid!!)
            FirebaseMessaging.getInstance().subscribeToTopic(productData.prdId!!);
        }

        val myDocu = db.collection("products").document(productData.prdId!!).asLiveData<ProductData>()
        myDocu.update("notifyOnUserId", newList)
    }

//    fun updateUserLikePrdList(productData: ProductData){
//        var oldList = productData.notifyOnUserId
//        var newList = oldList!!.toMutableList()
//
//        if(oldList.contains(auth.uid)){
//            newList.remove(auth.uid)
//        }else{
//            newList.add(auth.uid!!)
//        }
////        if(isButtonActive){
////            newList.remove(auth.uid)
////        }else{
////            newList.add(auth.uid!!)
////        }
//
//        val myDocu = db.collection("products").document(productData.prdId!!).asLiveData<ProductData>()
//        myDocu.update("notifyOnUserId", newList)
//    }



    fun updateAuctionStatus(status: Int, prdId: String){
        val myDocu = db.collection("products").document(prdId!!).asLiveData<ProductData>()
        val task = myDocu.update( "auctionProgressStatus" , status)
//        task.observe(lifecycleOwner, Observer { taskResult ->
//
//            Log.d(ContentValues.TAG, "aa      " + prdId)
//            Log.d(ContentValues.TAG, "1111 " +taskResult.data)
//            Log.d(ContentValues.TAG, "1111222 " +taskResult.exception)
//            Log.d(ContentValues.TAG, "111331 " +taskResult.status)
//        })
    }

    fun getPrdDataByPid(pid: String) : MutableLiveData<ProductData>{
        val myQuery = db.collection("products").document(pid).asLiveData<ProductData>()
        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<ProductData> ->
            if(resource.data !== null) {

                Log.d(ContentValues.TAG, "dfdfdfffffffffffffffff " + resource.data!!.prdId)

                productPidData.postValue(resource.data!!)
            }
        })
        return productPidData
    }

    fun getPurchasePrdList(): MutableLiveData<List<ProductData>> {

        val myQuery = db.collection("products").orderBy("endDate", Query.Direction.DESCENDING) .asLiveData<ProductData>()
        myQuery.observe(lifecycleOwner, Observer { resource: FirestoreResource<List<ProductData>> ->
            if(resource.data !== null) {
                val data: MutableList<ProductData>? = resource.data!!.toMutableList()

//                resource.data?.forEach {
//                    if((!it.highestBuyUserId!!.contains(auth.uid!!)) || (it.auctionProgressStatus != 3)){
//                        data!!.remove(it)
//                    }
////                    if(it.auctionProgressStatus != 3){
////                        data!!.remove(it)
////                    }
//                }
                var a = resource.data?.filter {
                    it.auctionProgressStatus == 3
                }
                var b = a?.filter {
                   it.highestBuyUserId == auth.uid!!
                }
                Log.d("aaaaaaa", a.toString())
                Log.d("bbbbbbb", b.toString())
                productUserPurchaseList.postValue(b!!)
            }
        })
        return productUserPurchaseList

    }

    fun getUserBidPrdList(): MutableLiveData<List<ProductData>> {


        val myPrdQuery = db.collection("products").orderBy("endDate", Query.Direction.DESCENDING).asLiveData<ProductData>()
        myPrdQuery.observe(lifecycleOwner, Observer{ resource: FirestoreResource<List<ProductData>> ->
            if(resource.data !== null) {
                var prddata: MutableList<ProductData> = resource.data!!.toMutableList()
                val userDoc = db.collection("users").document(auth.uid!!).asLiveData<UserData>()
                userDoc.observe(lifecycleOwner, Observer{ udata: FirestoreResource<UserData> ->
                    if(udata.data !== null){
                        if(udata.data!!.attendAuctionList !== null){

                            udata.data!!.attendAuctionList!!.forEach {
                                FirebaseMessaging.getInstance().subscribeToTopic(it+"bid")
                            }
                            productUserBidList.postValue(prddata.filter {
                                udata.data!!.attendAuctionList!!.contains(it.prdId)
                            })
                        }
                    }
                })



            }
        })

        return productUserBidList
    }

}