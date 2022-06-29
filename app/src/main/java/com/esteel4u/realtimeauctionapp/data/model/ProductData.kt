package com.esteel4u.realtimeauctionapp.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel

data class ProductData(
    var auctionType: Int? = null,  //1 프리미엄 2 옥션 3 아울랫 4 패키지
    var prdId: String? = null,  //제품 아이디 -> primary key
    var prdNo: String? = null,  //품번
    var prdName: String? = null,  //품명
    var substSpec: String? = null,  //
    var prdThk: Double? = null,  //두께
    var prdWth: Double? = null,  //폭
    var prdWgt: Double? = null,  //무게
    var prdTotClsSeqNm: String? = null,  //등급
    var worksCode: String? = null,  //광양, 판교
    var startDate: String? = null,  //경매시작일
    var endDate: String? = null,    //경매종료일
    var auctionProgressStatus: Int? = null,// 경매진행구분 1대기 2진행중 3종료
    var notifyOnUserId: List<UserData>? = null   //경매알림설정 유저아이디 리스트

): FirestoreModel()