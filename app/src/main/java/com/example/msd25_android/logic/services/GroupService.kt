package com.example.msd25_android.logic.services

import android.app.Application
import android.icu.math.BigDecimal
import androidx.lifecycle.AndroidViewModel
import com.example.msd25_android.API_URL
import com.example.msd25_android.logic.BackendResponse
import com.example.msd25_android.logic.data.models.Expense
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.logic.data.models.PayInfos
import kotlinx.serialization.Serializable

class GroupService(private val application: Application): AndroidViewModel(application) {

    private val requestHandler = com.example.msd25_android.requestHandler

    private val url = "$API_URL/group"

    suspend fun getGroupInfo(groupId: Long, onResponse: (BackendResponse<Group>) -> Unit) {

        @Serializable
        data class ReqObj(
            val groupId: Long
        )

        requestHandler.post(
            context = application,
            url = "$url/getinfo",
            postObject = ReqObj(groupId),
            onResponse = onResponse
        )
    }

    suspend fun createGroup(group: Group, onResponse: (BackendResponse<Group>) -> Unit) {

        requestHandler.post(
            context = application,
            url = "$url/post",
            postObject = group,
            onResponse = onResponse,
        )
    }

    suspend fun deleteGroup(groupId: Long, onResponse: (BackendResponse<String>) -> Unit) {

        @Serializable
        data class ReqObj(
            val groupId: Long
        )

        requestHandler.post(
            context = application,
            url = "$url/delete",
            postObject = ReqObj(groupId),
            onResponse = onResponse
        )
    }

    suspend fun getExpenses(groupId: Long, onResponse: (BackendResponse<List<Expense>>) -> Unit) {

        @Serializable
        data class ReqObj(
            val groupId: Long
        )

        requestHandler.post(
            context = application,
            url = "$url/getexpenses",
            postObject = ReqObj(groupId),
            onResponse = onResponse
        )
    }

    suspend fun postExpense(expense: Expense, onResponse: (BackendResponse<Expense>) -> Unit) {

        requestHandler.post(
            context = application,
            url = "$url/postexpense",
            postObject = expense,
            onResponse = onResponse
        )
    }

    suspend fun getPersonalBalance(userId: Long, groupId: Long, onResponse: (BackendResponse<BigDecimal>) -> Unit) {

        @Serializable
        data class ReqObj(
            val userId: Long,
            val groupId: Long
        )

        requestHandler.post(
            context = application,
            url = "$url/personalbalance",
            postObject = ReqObj(userId, groupId),
            onResponse = onResponse
        )
    }

    suspend fun getPaymentInfo(groupId: Long, onResponse: (BackendResponse<PayInfos>) -> Unit) {

        @Serializable
        data class ReqObj(
            val groupId: Long
        )

        requestHandler.post(
            context = application,
            url = "$url/paymentinfo",
            postObject = ReqObj(groupId),
            onResponse = onResponse
        )
    }

    suspend fun payDebtsToGroup(groupId: Long, onResponse: (BackendResponse<String>) -> Unit) {

        @Serializable
        data class ReqObj(
            val groupId: Long
        )

        requestHandler.post(
            context = application,
            url = "$url/paydebts",
            postObject = ReqObj(groupId),
            onResponse = onResponse
        )
    }
}