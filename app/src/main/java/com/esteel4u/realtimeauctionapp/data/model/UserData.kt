package com.esteel4u.realtimeauctionapp.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel
import lombok.Data

@Data
data class UserData(
    var uid: String? = null,
    var gcsCompCode: String? = null,
    var userName: String? = null
): FirestoreModel()