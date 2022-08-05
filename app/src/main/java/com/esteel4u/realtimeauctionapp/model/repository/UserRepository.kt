package com.esteel4u.realtimeauctionapp.model.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.esteel4u.realtimeauctionapp.model.data.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ptrbrynt.firestorelivedata.asLiveData

class UserRepository(val context: Context, val lifecycleOwner: LifecycleOwner) {

    var isSignIn = MutableLiveData<Boolean>()
    var userInfo = MutableLiveData<UserData>()
    private var auth = Firebase.auth
    private val db = Firebase.firestore

    fun signIn(eMail: String, password: String):MutableLiveData<Boolean> {
        auth.signInWithEmailAndPassword(eMail, password).addOnCompleteListener { task ->

            isSignIn.value = task.isSuccessful


        }
        return isSignIn

    }




    fun setAlarmOnOff(ischecked : String) {
        val myDocu = db.collection("users").document(auth.currentUser!!.uid).asLiveData<UserData>()
        val task = myDocu.update( "setAlarm" , ischecked)
    }

    fun getUserInfo():LiveData<UserData> {
        auth.currentUser?.let { user ->

            val docRef = db.collection("users").document(user.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        userInfo.value = UserData(
                            user.uid,
                            document.get("userId")as String,
                            document.get("gcsCompCode") as String,
                            document.get("userName") as String,
                            document.get("setAlarm") as String
                        )
                        //UserData.getInstance(user.uid, document.get("userId")as String,  document.get("gcsCompCode") as String,   document.get("userName") as String)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
        return userInfo
    }
    fun signOut() {
        auth.signOut()
    }
}