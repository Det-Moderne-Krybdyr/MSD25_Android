package com.example.msd25_android.logic.data.user

import androidx.room.Embedded
import androidx.room.Relation
import com.example.msd25_android.logic.data.notification.Notification

data class UserWithNotifications(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipientId"
    )
    val groups: List<Notification>
)
