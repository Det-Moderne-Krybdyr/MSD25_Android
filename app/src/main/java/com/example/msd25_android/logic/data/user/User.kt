package com.example.msd25_android.logic.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val email: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    val password: String,
    val birthdate: Date,
)