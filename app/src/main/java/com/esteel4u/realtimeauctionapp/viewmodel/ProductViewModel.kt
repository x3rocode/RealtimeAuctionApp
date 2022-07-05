package com.esteel4u.realtimeauctionapp.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.esteel4u.realtimeauctionapp.data.repository.ProductRepository
import com.esteel4u.realtimeauctionapp.data.repository.UserRepository

class ProductViewModel (val lifecycleOwner: LifecycleOwner): ViewModel() {
    private val repository = ProductRepository(lifecycleOwner)
    private var _prdData = MutableLiveData<List<ProductData>>()
    val prdData: MutableLiveData<List<ProductData>> get() = _prdData

    init {
        _prdData = repository.getAllPrdList()
    }

    fun getALlPrdList():MutableLiveData<List<ProductData>> {
        return repository.getAllPrdList()
    }

    fun getTodayPrdList():MutableLiveData<List<ProductData>> {
        return repository.getTodayAuctionList()
    }

    class Factory(val lifecycleOwner: LifecycleOwner) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductViewModel(lifecycleOwner) as T
        }
    }

}