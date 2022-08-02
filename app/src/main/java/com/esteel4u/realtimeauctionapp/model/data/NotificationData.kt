package com.esteel4u.realtimeauctionapp.model.data

import lombok.Data

@Data
data class NotificationData(
    val title: String,
    val message: String,
    val tag: String,
    val prdId: String
)

@Data
data class PushNotificationData (
    val data: NotificationData,
    val to: String
)