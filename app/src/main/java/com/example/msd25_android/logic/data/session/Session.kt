package com.example.msd25_android.logic.data.session

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.msd25_android.logic.data.user.User

@Entity(
    tableName = "sessions",
    primaryKeys = ["userId", "token"],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])]
)
data class Session(
    val userId: Long,
    val token: String
)
