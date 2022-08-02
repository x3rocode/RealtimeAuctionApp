package com.esteel4u.realtimeauctionapp.model.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.ptrbrynt.firestorelivedata.FirestoreModel

data class ProductData(
    var auctionType: Int? = 1,  //1 프리미엄 2 옥션 3 아울랫 4 패키지
    var prdId: String? = null,  //제품 아이디 -> primary key
    var prdNo: String? = null,  //품번
    var prdName: String? = null,  //품명
    var substSpec: String? = null,  //규격
    var prdThk: Double? = null,  //두께
    var prdWth: Int? = null,  //폭
    var prdWgt: Int? = null,  //무게
    var prdTotClsSeqNm: Int? = 1,  //등급 1 주문외1급 2주문외2급
    var worksCode: String? = null,  //광양, 판교
    var startDate: Timestamp? = null,  //경매시작일
    var endDate: Timestamp? = null,    //경매종료일
    var auctionProgressStatus: Int? = 1,// 경매진행구분 1진행중 2진행예정 3종료
    var notifyOnUserId: List<String>?  = null,   //경매알림설정 유저아이디 리스트
    var highestBuyUserId: String? = null,
    var bidPrice: Int? = null

): FirestoreModel(),  Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(auctionType)
        parcel.writeString(prdId)
        parcel.writeString(prdNo)
        parcel.writeString(prdName)
        parcel.writeString(substSpec)
        parcel.writeValue(prdThk)
        parcel.writeValue(prdWth)
        parcel.writeValue(prdWgt)
        parcel.writeValue(prdTotClsSeqNm)
        parcel.writeString(worksCode)
        parcel.writeParcelable(startDate, flags)
        parcel.writeParcelable(endDate, flags)
        parcel.writeValue(auctionProgressStatus)
        parcel.writeStringList(notifyOnUserId)
        parcel.writeString(highestBuyUserId)
        parcel.writeValue(bidPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductData> {
        override fun createFromParcel(parcel: Parcel): ProductData {
            return ProductData(parcel)
        }

        override fun newArray(size: Int): Array<ProductData?> {
            return arrayOfNulls(size)
        }
    }
}
