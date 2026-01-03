package com.example.msd25_android.logic.data.models

import com.example.msd25_android.logic.data.serialize.ISODateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long = 0,
    val name: String = "",
    val email: String = "",
    val phone_number: String = "",
    val password: String = "",
    @Serializable(with = ISODateSerializer::class)
    @Contextual
    val birthdate: Long = 0,
    @Serializable(with = ISODateSerializer::class)
    @Contextual
    val created_on: Long = 0,
)