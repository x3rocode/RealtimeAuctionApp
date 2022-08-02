package com.esteel4u.realtimeauctionapp.viewmodel

import androidx.lifecycle.*
import com.esteel4u.realtimeauctionapp.model.data.AuctionData
import com.esteel4u.realtimeauctionapp.model.repository.AuctionRepository

class AuctionViewModel (val lifecycleOwner: LifecycleOwner): ViewModel() {
    private val repository = AuctionRepository(lifecycleOwner)
    private var _actData = MutableLiveData<List<AuctionData>>()
    val actData: MutableLiveData<List<AuctionData>> get() = _actData

    fun setBid(price: Int, prdId: String, currentBuyUserToken: String) {
        return repository.setBid(price, prdId, currentBuyUserToken)
    }

    fun setBidFirst(price: Int, prdId: String) {
        return repository.setBidFirst(price, prdId)
    }

    fun getAuctionInfo(prdId: String):MutableLiveData<AuctionData> {
        return repository.getAuctionInfo(prdId)
    }

    fun getAllAuctionList():MutableLiveData<List<AuctionData>> {
        return repository.getAllAuctionList()
    }
    fun getUserAttendAuctionList() :MutableLiveData<List<AuctionData>> {
        return repository.getUserAttendAuctionList()
    }

    class Factory(val lifecycleOwner: LifecycleOwner) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuctionViewModel(lifecycleOwner) as T
        }
    }

}