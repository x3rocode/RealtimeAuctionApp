package com.esteel4u.realtimeauctionapp.model

import com.ptrbrynt.firestorelivedata.FirestoreModel

data class Product(
    var auctionType: String,
    var prdId: String,
    var prdNo: String,
    var prdName: String,
    var substSpec: String,
    var prdThk: Double,
    var prdWth: Double,
    var prdLth: Double,
    var prdWgt: Double,
    var worksCode: String,
    var startDate: String,
    var endDate: String,
    var notiOnUserId: String
): FirestoreModel()
