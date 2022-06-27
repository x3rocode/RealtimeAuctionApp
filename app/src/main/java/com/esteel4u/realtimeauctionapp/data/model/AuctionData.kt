package com.esteel4u.realtimeauctionapp.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel
import lombok.Data
import java.sql.Timestamp

@Data
data class AuctionData(
    var productId: String,
    var bidPrice: Int,
    var buyUserId: String,
    var bidTimeStamp: Timestamp
): FirestoreModel()
