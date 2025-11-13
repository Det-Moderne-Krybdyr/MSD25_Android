package com.example.msd25_android.logic.data.expense

import androidx.room.Embedded
import androidx.room.Relation
import com.example.msd25_android.logic.data.user.User

data class ExpenseWithShares(
    @Embedded
    val expense: Expense,
    @Relation(
        parentColumn = "id",
        entityColumn = "expenseId"
    )
    val shares: List<ExpenseShare>,
    @Relation(
        parentColumn = "paidBy",
        entityColumn = "id"
    )
    val user: User
)
