package com.example.msd25_android.logic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.msd25_android.logic.data.expense.Expense
import com.example.msd25_android.logic.data.expense.ExpenseShare
import com.example.msd25_android.logic.data.expense.ExpenseWithShares

@Dao
interface ExpenseDao {
    @Insert
    fun insertExpense(expense: Expense): Long

    @Update
    fun updateExpenseShares(shares: List<ExpenseShare>)
    @Insert
    fun insertExpenseShares(shares: List<ExpenseShare>)
    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    fun getExpenseWithShares(expenseId: Long): ExpenseWithShares?

    @Query("SELECT * FROM expenses WHERE id IN (:expenseIds)")
    fun getExpenseWithShares(expenseIds: Collection<Long>): List<ExpenseWithShares>?
}