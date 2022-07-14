package com.esteel4u.realtimeauctionapp.service

import android.util.Log
import com.esteel4u.realtimeauctionapp.data.model.PushData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class FcmPushService  {
    val JSON = MediaType.parse("application/json; charset=utf-8")//Post전송 JSON Type
    val url = "https://fcm.googleapis.com/fcm/send" //FCM HTTP를 호출하는 URL
    val serverKey =
        "BKzTd_pCLcJSM_2eseIGcBYoM_6K_xAI8J30CIQSH4p-DAsUoZXBCu-HiVc3PWNUNxW9b-OvrCCvly7aH3-0Agc"
    //Firebase에서 복사한 서버키
    var okHttpClient: OkHttpClient
    var gson: Gson

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid: String, title: String, message: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.i("토큰정보", token)
                    var pushData = PushData()
                    pushData.to = token                   //푸시토큰 세팅
                    pushData.notification?.title = title  //푸시 타이틀 세팅
                    pushData.notification?.body = message //푸시 메시지 세팅

                    var body = RequestBody.create(JSON, gson?.toJson(pushData)!!)
                    var request = Request
                        .Builder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + serverKey)
                        .url(url)       //푸시 URL 세팅
                        .post(body)     //pushDTO가 담긴 body 세팅
                        .build()
                    okHttpClient?.newCall(request)?.enqueue(object : Callback {//푸시 전송
                    override fun onFailure(call: Call?, e: IOException?) {
                    }

                        override fun onResponse(call: Call?, response: Response?) {
                            println(response?.body()?.string())  //요청이 성공했을 경우 결과값 출력
                        }
                    })
                }
            })
    }

}
