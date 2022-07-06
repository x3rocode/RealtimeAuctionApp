package com.esteel4u.realtimeauctionapp.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule.Companion.gcsCompCode
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule.Companion.uid
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule.Companion.userId
import lombok.Data
import javax.inject.Singleton

@Data
@Singleton
data class UserData(
    var uid: String? = null,
    var userId: String? = null,
    var gcsCompCode: String? = null,
    var userName: String? = null
): FirestoreModel(){
//    companion object {
//        @Volatile
//        @JvmStatic
//        private var INSTANCE: UserData? = null
//
//        @JvmStatic
//        @JvmOverloads
//        fun getInstance(uid: String = "", userId : String = "", gcsCompCode: String = "", username: String="" ): UserData = INSTANCE ?: synchronized(this) {
//            INSTANCE ?: UserData(uid, userId,gcsCompCode,username).also { INSTANCE = it }
//        }
//
//
//        @JvmStatic
//        @JvmOverloads
//        fun getInstance(user: UserData): UserData = INSTANCE ?: synchronized(this) {
//            INSTANCE ?: UserData(user.uid, user.userId,user.gcsCompCode,user.userName).also { INSTANCE = it }
//        }
//    }
}