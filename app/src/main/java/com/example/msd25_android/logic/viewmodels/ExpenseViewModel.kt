package com.example.msd25_android.logic.viewmodels

import android.app.Application
import android.icu.math.BigDecimal
import androidx.lifecycle.AndroidViewModel
import com.example.msd25_android.logic.BackendResponse
import com.example.msd25_android.logic.data.AppDatabase
import com.example.msd25_android.logic.data.expense.BalanceBetweenUsers
import com.example.msd25_android.logic.data.expense.Expense
import com.example.msd25_android.logic.data.expense.ExpenseShare
import com.example.msd25_android.logic.data.expense.ExpenseWithShares
import kotlinx.datetime.Clock

class ExpenseViewModel(application: Application): AndroidViewModel(application) {
    val expenseDao = AppDatabase.Companion.getDatabase(application).expenseDao()
    val groupDao = AppDatabase.Companion.getDatabase(application).groupDao()

    val userDao = AppDatabase.Companion.getDatabase(application).userDao()

    fun createExpense(expense: Expense, shares: List<ExpenseShare>) {
        val id = expenseDao.insertExpense(expense)
        val newShares = shares.map {
            it.copy(
                expenseId = id,
            )
        }
        expenseDao.insertExpenseShares(newShares)
    }

    fun getExpenseWithShares(id: Long): BackendResponse<ExpenseWithShares> {
        return BackendResponse.create(expenseDao.getExpenseWithShares(id))
    }

    fun getExpensesWithShares(ids: Collection<Long>): BackendResponse<List<ExpenseWithShares>> {
        return BackendResponse.create(expenseDao.getExpenseWithShares(ids))
    }

    fun getAmountOwed(groupId: Long, phoneNumber: String): BackendResponse<BigDecimal> {
        val user = userDao.getUserByPhone(phoneNumber)
        if (user == null) return BackendResponse.create(null)

        // get expenses
        val res = groupDao.getGroupWithExpenses(groupId)
        if (res == null) return BackendResponse.create(null)
        // get all expenses with all shares
        val expensesRes = getExpensesWithShares(res.expenses.map { it.id })
        if (!expensesRes.success) return BackendResponse.create(null)

        val expensesPaidByUser = expensesRes.data!!.filter { it.user.phoneNumber == phoneNumber }
        val expensesOwedByUser = expensesRes.data
            .mapNotNull { expense -> expense.shares
            .find { share -> share.userId == user.id } }

        val amountPaidByUser = expensesPaidByUser.fold(BigDecimal.ZERO) { acc, expense ->
            expense.shares.fold(BigDecimal.ZERO) { shareAcc, share ->
                shareAcc.add(share.amountOwed)
            }
        }

        val amountOwedByUser = expensesOwedByUser.fold(BigDecimal.ZERO) { acc, share ->
            acc.add(share.amountOwed)
        }

        return BackendResponse.create(amountPaidByUser.subtract(amountOwedByUser))
    }

    fun getBalanceBetweenUsers(groupId: Long, user1Phone: String, user2Id: Long): BackendResponse<BalanceBetweenUsers> {
        val user1 = userDao.getUserByPhone(user1Phone)
        val user2 = userDao.getUserById(user2Id)
        if (user1 == null || user2 == null) return BackendResponse.create(null)
        val user1Id = user1.id

        // get expenses
        val res = groupDao.getGroupWithExpenses(groupId)
        if (res == null) return BackendResponse.create(null)
        // get all expenses with all shares
        val expensesRes = getExpensesWithShares(res.expenses.map { it.id })
        if (!expensesRes.success) return BackendResponse.create(null)

        // get everything paid by user1, to user2
        val amountPaidByUser1 = expensesRes.data!!.filter { expense ->
            expense.user.id == user1Id
        }.mapNotNull { expense -> expense.shares.find { share ->
            share.userId == user2Id
        } }.fold(BigDecimal.ZERO) { acc, share ->
            acc.add(share.amountOwed)
        }

        // get everything paid by user2, to user1
        val amountPaidByUser2 = expensesRes.data.filter { expense ->
            expense.user.id == user2Id
        }.mapNotNull { expense -> expense.shares.find { share ->
            share.userId == user1Id
        } }.fold(BigDecimal.ZERO) { acc, share ->
            acc.add(share.amountOwed)
        }

        return BackendResponse.create(BalanceBetweenUsers(user1, user2, amountPaidByUser2.subtract(amountPaidByUser1)))
    }

    fun payAllDebts(groupId: Long, userPhoneNumber: String): BackendResponse<Unit> {
        val group = groupDao.getGroupWithMembers(groupId)
        if (group == null) return BackendResponse.create(null)
        val user = group.members.first { it.phoneNumber == userPhoneNumber }
        val balances = mutableListOf<BalanceBetweenUsers>()
        group.members.forEach { member ->
            val debtRes = getBalanceBetweenUsers(groupId, member.phoneNumber, user.id)
            if (debtRes.success)
                balances.add(debtRes.data!!)
        }

        val debts = balances.filter { it.amount < BigDecimal.ZERO }
        val expense = Expense(
            description = "settling their debt",
            paidBy = user.id,
            groupId = groupId,
            createdOn = Clock.System.now()
        )

        val shares = mutableListOf<ExpenseShare>()
        debts.forEach {
            shares.add(ExpenseShare(
                userId = it.user1.id,
                amountOwed = BigDecimal.ZERO.subtract(it.amount)
            ))
        }

        createExpense(expense, shares)
        val successMsg = debts.fold("Paid debts: ") { acc, it ->
            acc.plus("${it.amount.format(-1, 2)} to ${it.user1.name} \n")
        }
        return BackendResponse.create(Unit, successMsg = successMsg)
    }
}