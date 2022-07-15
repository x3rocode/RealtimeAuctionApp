package com.esteel4u.realtimeauctionapp.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.esteel4u.realtimeauctionapp.data.model.AuctionData
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.esteel4u.realtimeauctionapp.data.repository.AuctionRepository
import com.esteel4u.realtimeauctionapp.data.repository.ProductRepository
import com.esteel4u.realtimeauctionapp.data.repository.UserRepository

class AuctionViewModel (val lifecycleOwner: LifecycleOwner): ViewModel() {
    private val repository = AuctionRepository(lifecycleOwner)
    private var _actData = MutableLiveData<List<AuctionData>>()
    val actData: MutableLiveData<List<AuctionData>> get() = _actData

    fun setBid(price: Int, prdId: String, currentBuyUserToken: String) {
        return repository.setBid(price, prdId, currentBuyUserToken)
    }

    fun getAuctionInfo(prdId: String):MutableLiveData<AuctionData> {
        return repository.getAuctionInfo(prdId)
    }


    class Factory(val lifecycleOwner: LifecycleOwner) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuctionViewModel(lifecycleOwner) as T
        }
    }

}