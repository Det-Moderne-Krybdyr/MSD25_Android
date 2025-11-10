package com.example.msd25_android.logic.data.expense

import androidx.room.Embedded
import androidx.room.Relation

data class ExpenseWithShares(
    @Embedded
    val expense: Expense,
    @Relation(
        parentColumn = "id",
        entityColumn = "expenseId"
    )
    val shares: List<ExpenseShare>
)
