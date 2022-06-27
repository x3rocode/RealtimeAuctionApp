package com.esteel4u.realtimeauctionapp.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esteel4u.realtimeauctionapp.data.repository.UserRepository

class LoginViewModel(val context: Context): ViewModel() {
    private val repository = UserRepository()
    private var _isSignIn = MutableLiveData<Boolean>()
    val isSignIn: LiveData<Boolean>
        get() = _isSignIn

    init {
        _isSignIn = repository.isSignIn
    }

    fun signIn(eMail: String, password: String) {
        Log.d(ContentValues.TAG, "ddfasdfasdfasdf f fdfdf " + eMail)
        Log.d(ContentValues.TAG, "ddfasdfasdfasdf f fdfdf " + password)
        repository.signIn(eMail, password)
    }

    class Factory(val context: Context) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(context) as T
        }
    }
}