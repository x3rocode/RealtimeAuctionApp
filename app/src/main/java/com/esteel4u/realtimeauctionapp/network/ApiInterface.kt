package com.esteel4u.realtimeauctionapp.network

import com.esteel4u.realtimeauctionapp.model.data.PushNotificationData
import com.esteel4u.realtimeauctionapp.network.ApiClient.CONTENT_TYPE
import com.esteel4u.realtimeauctionapp.network.ApiClient.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Call


import retrofit2.http.*
object ApiClient{
    const val FCM_URL = "https://fcm.googleapis.com/"
    const val SERVER_KEY = "AAAABgtIXgk:APA91bGneG8p25Th1FkDduI2dCkul31nVrgB3jgYMUU-m-2Hz8EMJ86mPbadrgGd0_UZyMXgaayOTn5TE9n13e8Ev0cXFWv9EUxrN3KBPufgETCnrKmGmJhrYo_qvAVtZ6WRDKu2rsdO"
    const val CONTENT_TYPE = "application/json"
}

interface ApiInterface {


    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    fun sendToLoser(
        @Body notification: PushNotificationData
    ): Call<ResponseBody>
}