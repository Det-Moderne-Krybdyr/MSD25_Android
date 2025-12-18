package com.example.msd25_android.logic.data.models

import android.icu.math.BigDecimal
import com.example.msd25_android.logic.data.serialize.BigDecimalSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseShare(
    val expense_id: Long = 0,
    val user_id: Long = 0,
    val user: User = User(),
    val settled: Boolean = false,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal = BigDecimal.ZERO,
)