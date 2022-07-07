package com.esteel4u.realtimeauctionapp.data.repository

import android.content.ContentValues
import android.os.Build.ID
import android.provider.ContactsContract.DisplayNameSources.NICKNAME
import android.provider.SimPhonebookContract.SimRecords.PHONE_NUMBER
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.analytics.FirebaseAnalytics.Event.SIGN_UP
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.ptrbrynt.firestorelivedata.asLiveData

class UserRepository {

    var isSignIn = MutableLiveData<Boolean>()
    var userInfo = MutableLiveData<UserData>()

    private var auth = Firebase.auth
    private val db = Firebase.firestore

    fun signIn(eMail: String, password: String) {
        auth.signInWithEmailAndPassword(eMail, password).addOnCompleteListener { task ->

            isSignIn.value = task.isSuccessful

            val myCollection = db.collection("products") .asLiveData<ProductData>()

//            myCollection.add(ProductData(1, "S2126218190719008412312g", "FFF", "H123123",
//            "rrrr", 10.0,10.0,100.0, 1, "K", Timestamp.now(), Timestamp.now(),
//            0, listOf("flprE6zO4OgMrFLUJwcNoMZxsbE3", "6bPxMnRfvrZhNkMcagRlQAJytI52")
//            ))
        }

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
                            document.get("userName") as String
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