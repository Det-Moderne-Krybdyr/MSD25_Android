package com.example.msd25_android.logic.data.models

import com.example.msd25_android.logic.data.serialize.ISODateSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: Long = 0,
    val name: String = "",
    val members: List<User> = listOf(),
    @Serializable(with = ISODateSerializer::class)
    val created_on: Long = 0,
)