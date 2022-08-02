package com.esteel4u.realtimeauctionapp.viewmodel

import androidx.lifecycle.*
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.model.repository.ProductRepository
import org.joda.time.DateTime


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

    fun getUserLikePrdList():MutableLiveData<List<ProductData>> {
        return repository.getUserLikePrdList()
    }

    fun updateUserLikePrdList(isButtonActive: Boolean, productData: ProductData){
        repository.updateUserLikePrdList(isButtonActive, productData)
    }

    fun getPrdDataByPid(pid: String): MutableLiveData<ProductData>{
        return repository.getPrdDataByPid(pid)
    }

    fun getPurchasePrdList():MutableLiveData<List<ProductData>> {
        return repository.getPurchasePrdList()
    }

    fun setBuyUser(prdId: String){
        repository.setBuyUser(prdId)
    }

    fun setBidPrice(bidPrice: Int, prdId:String){
        repository.setBidPrice(bidPrice, prdId)
    }

    fun getPrdListByDate(dateTime: DateTime):MutableLiveData<List<ProductData>>  {
        return repository.getPrdlistByDate(dateTime)
    }

    fun getUserBidPrdList():MutableLiveData<List<ProductData>> {
        return repository.getUserBidPrdList()
    }


    class Factory(val lifecycleOwner: LifecycleOwner) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductViewModel(lifecycleOwner) as T
        }
    }

}