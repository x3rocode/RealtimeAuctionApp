package com.esteel4u.realtimeauctionapp.model

import com.ptrbrynt.firestorelivedata.FirestoreModel
import java.sql.Timestamp

data class Auction(
    var productId: String,
    var bidPrice: Int,
    var buyUserId: String,
    var bidTimeStamp: Timestamp
): FirestoreModel()
