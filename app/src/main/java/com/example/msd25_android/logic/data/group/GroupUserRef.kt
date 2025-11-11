package com.example.msd25_android.logic.data.group

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.msd25_android.logic.data.user.User

@Entity(
    tableName = "group_user_ref",
    primaryKeys = ["groupId", "userId"],
    foreignKeys = [
        ForeignKey(entity = Group::class, parentColumns = ["id"], childColumns = ["groupId"]),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
    ]
)
data class GroupUserRef(
    val groupId: Long,
    val userId: Long
)
