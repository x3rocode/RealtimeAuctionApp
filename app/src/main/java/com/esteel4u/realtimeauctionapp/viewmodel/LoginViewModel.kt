package com.esteel4u.realtimeauctionapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.esteel4u.realtimeauctionapp.Utils.ErrorMessage
import com.esteel4u.realtimeauctionapp.Utils.LoadState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ptrbrynt.firestorelivedata.Status
import java.util.regex.Matcher


