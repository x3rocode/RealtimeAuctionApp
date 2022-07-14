package com.esteel4u.realtimeauctionapp.data.model

data class PushData(
    var to: String? = null,                             //PushToken을 입력하는 부분 푸시를 받는 사용자
    var notification: Notification? = Notification()    //백그라운드 푸시 호출하는 변수
) {
    data class Notification(
        var body: String? = null,                       //백그라운드 푸시 메시지 내용
        var title: String? = null                       //백그라운드 푸시 타이틀
    )
}