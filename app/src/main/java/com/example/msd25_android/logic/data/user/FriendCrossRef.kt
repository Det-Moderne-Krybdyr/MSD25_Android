package com.example.msd25_android.logic.data.user

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "friend_cross_ref",
    primaryKeys = ["id", "friendId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["id"]),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["friendId"])
    ]
    )
data class FriendCrossRef(
    val id: Long,
    val friendId: Long,
)