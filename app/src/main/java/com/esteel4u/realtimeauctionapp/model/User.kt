package com.esteel4u.realtimeauctionapp.model

import com.ptrbrynt.firestorelivedata.FirestoreModel

data class User(
    var userId: String,
    var userPw: String,
    var gcsCompCode: String,
    var userName: String
): FirestoreModel()