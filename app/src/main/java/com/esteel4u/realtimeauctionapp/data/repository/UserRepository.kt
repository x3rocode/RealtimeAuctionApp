package com.esteel4u.realtimeauctionapp.data.repository

import android.content.ContentValues
import android.os.Build.ID
import android.provider.ContactsContract.DisplayNameSources.NICKNAME
import android.provider.SimPhonebookContract.SimRecords.PHONE_NUMBER
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.google.firebase.analytics.FirebaseAnalytics.Event.SIGN_UP
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {

    var isSignIn = MutableLiveData<Boolean>()
    var userInfo = MutableLiveData<UserData>()

    private var auth = Firebase.auth
    private val db = Firebase.firestore

    fun signIn(eMail: String, password: String) {
        auth.signInWithEmailAndPassword(eMail, password).addOnCompleteListener { task ->

            isSignIn.value = task.isSuccessful
        }
    }


    fun getUserInfo() {
        auth.currentUser?.let { user ->

            val docRef = db.collection("users").document(user.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    document?.let {
                        userInfo.value = UserData(
                            user.uid,
                            document.get("userId")as String,
                            document.get("gcsCompCode") as String,
                            document.get("userName") as String
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}