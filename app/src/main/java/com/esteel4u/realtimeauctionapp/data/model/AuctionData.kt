package com.esteel4u.realtimeauctionapp.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel
import lombok.Data
import java.sql.Timestamp

@Data
data class AuctionData(
    var productId: String? = null,
    var bidPrice: Int? = 0, //입찰금액
    var buyUserId: String? = null,
    var buyUserToken: String? = null
): FirestoreModel()
