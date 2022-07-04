package com.esteel4u.realtimeauctionapp.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel
import lombok.Data


@Data
data class TestData(
var aa: String? = null,
var bb: String? = null
): FirestoreModel()
