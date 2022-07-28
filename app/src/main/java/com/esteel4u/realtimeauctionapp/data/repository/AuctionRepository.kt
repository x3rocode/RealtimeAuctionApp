package com.esteel4u.realtimeauctionapp.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.esteel4u.realtimeauctionapp.data.model.AuctionData
import com.esteel4u.realtimeauctionapp.data.model.BidUserList
import com.esteel4u.realtimeauctionapp.data.model.NotificationData
import com.esteel4u.realtimeauctionapp.data.model.PushNotificationData
import com.esteel4u.realtimeauctionapp.network.ApiInterface
import com.esteel4u.realtimeauctionapp.network.RetrofitInstance
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AuctionRepository(val lifecycleOwner: LifecycleOwner) {
    private var auctioninfo = MutableLiveData<AuctionData>()
    private var auth = Firebase.auth
    private val db = Firebase.firestore

    fun setBid(price: Int, prdId: String, currentBuyUserToken: String){
        val myDocu = db.collection("auctions").document(prdId!!).asLiveData<AuctionData>()

        //subscrib -> message is sended by server when auction is done
        FirebaseMessaging.getInstance().subscribeToTopic(prdId!! + "bid")

        //update highest price, userid, token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            var token = task.result
            myDocu.update(mapOf(Pair("highestBuyUserId", auth.uid!!), Pair("bidPrice", price), Pair("buyUserToken", token!!)))
        })

        //add in list
        val userListDoc = db.collection("auctions").document(prdId!!).collection("bidUserList").asLiveData<BidUserList>()
        userListDoc.add(BidUserList(price, auth.uid))

        //send message to current user
        if(currentBuyUserToken.isNotEmpty()){
            var senddata = PushNotificationData(
                NotificationData("내가 참여한 경매에 더 높은 금액이 입찰되었어요.", "이대로 당하고 있을건가요?", "loser", prdId!!),
                currentBuyUserToken)
            sendNotification(senddata)
        }

    }

    fun setBidFirst(price: Int, prdId: String){
        val myDocu = db.collection("auctions").document(prdId!!).asLiveData<AuctionData>()
        //add highest price, userid, token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            var token = task.result
            myDocu.update(mapOf(Pair("highestBuyUserId", auth.uid!!), Pair("bidPrice", price), Pair("buyUserToken", token!!)))
        })

        //add in list
        val userListDoc = db.collection("auctions").document(prdId!!).collection("bidUserList").asLiveData<BidUserList>()
        userListDoc.add(BidUserList(price, auth.uid))
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

    private fun sendNotification(notification: PushNotificationData){
        val apiInterface = RetrofitInstance.getRetrofitInstance()
        var sendToLoserService: ApiInterface = apiInterface.create(ApiInterface::class.java)

        sendToLoserService.sendToLoser(notification).enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG,t.toString() + "응애응애 실패야")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(TAG,"응애응애 성공이야222")
                Log.d(TAG,response.toString())
            }
        })
    }
}
