package com.esteel4u.realtimeauctionapp.data.model

import lombok.Data

@Data
data class NotificationData(
    val title: String,
    val message: String,
    val isScheduled: Boolean
)

@Data
data class PushNotificationData (
    val data: NotificationData,
    val to: String
)