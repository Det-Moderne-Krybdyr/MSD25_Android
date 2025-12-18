package com.example.msd25_android.logic.data.models

import com.example.msd25_android.logic.data.models.ExpenseShare
import com.example.msd25_android.logic.data.serialize.ISODateSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    val id: Long = 0,
    val description: String = "",
    val paid_by_id: Long = 0,
    val paid_by_user: User = User(),
    val group_id: Long = 0,
    val hidden:Boolean = false,
    @Serializable(with = ISODateSerializer::class)
    val created_on: Long = 0,
    val expense_shares: List<ExpenseShare> = listOf(),
)