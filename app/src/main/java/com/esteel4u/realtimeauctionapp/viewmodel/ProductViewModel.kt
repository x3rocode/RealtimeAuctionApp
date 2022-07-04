package com.esteel4u.realtimeauctionapp.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.esteel4u.realtimeauctionapp.data.repository.ProductRepository
import com.esteel4u.realtimeauctionapp.data.repository.UserRepository

class ProductViewModel (): ViewModel() {
    private val repository = ProductRepository()
    private var _prdData = MutableLiveData<List<ProductData>>()
    val prdData: LiveData<List<ProductData>> get() = _prdData
//
//    init {
//        _prdData = repository.getAllPrdData()
//    }
//
//    fun getALlPrdData() {
//        repository.getAllPrdData()
//    }

}