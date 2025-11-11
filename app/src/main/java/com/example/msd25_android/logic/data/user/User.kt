package com.example.msd25_android.logic.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant


@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true), Index(value = ["phone_number"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    var name: String = "",
    val email: String = "",
    @ColumnInfo(name = "phone_number") val phoneNumber: String = "",
    val password: String = "",
    var birthdate: Instant,
)