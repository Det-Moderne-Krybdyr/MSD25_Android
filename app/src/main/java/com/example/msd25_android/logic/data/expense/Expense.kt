package com.example.msd25_android.logic.data.expense

import android.icu.math.BigDecimal
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.data.user.User
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["paidBy"]),
        ForeignKey(entity = Group::class, parentColumns = ["id"], childColumns = ["groupId"])
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val paidBy: Long,
    val groupId: Long,
    val createdOn: Instant
)
