package com.example.msd25_android.logic.data.models

import com.example.msd25_android.logic.data.serialize.ISODateSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val userId: Long = 0,
    val token: String = "",
    @Serializable(with = ISODateSerializer::class)
    val created_on: Long = 0,
)