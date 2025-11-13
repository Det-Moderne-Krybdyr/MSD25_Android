package com.example.msd25_android.logic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.msd25_android.logic.BackendResponse
import com.example.msd25_android.logic.data.AppDatabase
import com.example.msd25_android.logic.data.expense.Expense
import com.example.msd25_android.logic.data.expense.ExpenseShare
import com.example.msd25_android.logic.data.expense.ExpenseWithShares

class ExpenseViewModel(application: Application): AndroidViewModel(application) {
    val dao = AppDatabase.Companion.getDatabase(application).expenseDao()

    fun createExpense(expense: Expense, shares: List<ExpenseShare>) {
        val id = dao.insertExpense(expense)
        shares.forEach { it.expenseId = id }
        dao.insertExpenseShares(shares)
    }

    fun getExpenseWithShares(id: Long): BackendResponse<ExpenseWithShares> {
        return BackendResponse.create(dao.getExpenseWithShares(id))
    }

    fun getExpensesWithShares(ids: Collection<Long>): BackendResponse<List<ExpenseWithShares>> {
        return BackendResponse.create(dao.getExpenseWithShares(ids))
    }
}