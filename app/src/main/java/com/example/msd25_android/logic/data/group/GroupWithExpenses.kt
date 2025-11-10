package com.example.msd25_android.logic.data.group

import androidx.room.Embedded
import androidx.room.Relation
import com.example.msd25_android.logic.data.expense.Expense

data class GroupWithExpenses(
    @Embedded
    val group: Group,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val expenses: List<Expense>
)
