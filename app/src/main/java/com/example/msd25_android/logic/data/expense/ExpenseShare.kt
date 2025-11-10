package com.example.msd25_android.logic.data.expense

import android.icu.math.BigDecimal
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.msd25_android.logic.data.user.User
import kotlinx.datetime.Instant

@Entity(
    tableName = "expense_shares",
    foreignKeys = [
        ForeignKey(entity = Expense::class, parentColumns = ["id"], childColumns = ["expenseId"]),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
    ]
)
data class ExpenseShare(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val expenseId: Long,
    val userId: Long,
    val amountOwed: BigDecimal,
    val isSettled: Boolean,
    val settledAt: Instant?
)
