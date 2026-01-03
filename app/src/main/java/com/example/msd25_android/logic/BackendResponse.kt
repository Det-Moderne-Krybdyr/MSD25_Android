package com.example.msd25_android.logic

import kotlinx.serialization.Serializable

@Serializable
data class BackendResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)