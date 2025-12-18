package com.example.msd25_android.logic.data.models

import android.icu.math.BigDecimal
import com.example.msd25_android.logic.data.serialize.BigDecimalSerializer
import kotlinx.serialization.Serializable

@Serializable
data class DebtTransfer (
    val firstFromId: Long = 0,
    val secondFromId: Long = 0,
    val toId: Long = 0,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal = BigDecimal.ZERO
)

@Serializable
data class PayInfo (
    val user: User = User(),
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal = BigDecimal.ZERO
)

@Serializable
data class PayInfos (
    val payer: User = User(),
    val infos: List<PayInfo> = listOf(),
    val transfers: List<DebtTransfer> = listOf()
)