package com.esteel4u.realtimeauctionapp.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.esteel4u.realtimeauctionapp.model.data.UserData
import com.esteel4u.realtimeauctionapp.model.repository.UserRepository

class LoginViewModel(val context: Context, val lifecycleOwner: LifecycleOwner): ViewModel() {
    private val repository = UserRepository(context, lifecycleOwner)
    private var _userInfo = MutableLiveData<UserData>()
    private var _isSignIn = MutableLiveData<Boolean>()

    val isSignIn: LiveData<Boolean>
        get() = _isSignIn

    val userInfo: LiveData<UserData>
        get()= _userInfo

    init {
        _isSignIn = repository.isSignIn
    }

    fun signIn(eMail: String, password: String) {

        repository.signIn(eMail, password)
    }

    fun logout(){

        repository.signOut()
    }

    fun getLoggedInUserInfo(): LiveData<UserData>{
        return repository.getUserInfo()
    }

    fun setAlarmOnOff(ischecked: String) {
        repository.setAlarmOnOff(ischecked)
    }

    class Factory(val context: Context, val lifecycleOwner: LifecycleOwner) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(context, lifecycleOwner) as T
        }
    }
}