package com.example.msd25_android.logic.data.user

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "friend_cross_ref",
    primaryKeys = ["firstId", "secondId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["firstId"]),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["secondId"])
    ]
    )
data class FriendCrossRef(
    val firstId: Long,
    val secondId: Long,
)