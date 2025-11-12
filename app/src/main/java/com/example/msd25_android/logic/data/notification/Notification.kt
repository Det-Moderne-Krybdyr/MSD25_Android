package com.example.msd25_android.logic.data.notification

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.msd25_android.logic.data.Converters
import com.example.msd25_android.logic.data.user.User

@Entity(
    tableName = "notifications",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["recipientId"])]
)
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val recipientId: Long,
    val type: Converters.NotificationType,
    val relatedId: Long,
    val message: String,
    val read: Boolean
)
