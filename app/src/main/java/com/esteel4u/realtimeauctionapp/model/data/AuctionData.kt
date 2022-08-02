package com.esteel4u.realtimeauctionapp.model.data

import com.ptrbrynt.firestorelivedata.FirestoreModel
import lombok.Data

@Data
data class AuctionData(
    var productId: String? = null,
    var bidPrice: Int? = 0, //입찰금액
    var highestBuyUserId: String? = null,
    var buyUserToken: String? = null,
    var bidUserList: MutableList<BidUserList>? = null
): FirestoreModel()

@Data
data class BidUserList(
    var bidPrice: Int? = 0,
    var bidUserId: String? = null
): FirestoreModel()


