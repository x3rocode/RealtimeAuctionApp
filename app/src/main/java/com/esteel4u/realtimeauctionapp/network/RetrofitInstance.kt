package com.esteel4u.realtimeauctionapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(ApiClient.FCM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }
}