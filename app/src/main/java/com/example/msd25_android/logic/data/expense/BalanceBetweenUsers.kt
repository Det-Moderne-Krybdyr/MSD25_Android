package com.example.msd25_android.logic.data.expense

import android.icu.math.BigDecimal
import com.example.msd25_android.logic.data.user.User

data class BalanceBetweenUsers(
    val user1: User,
    val user2: User,
    val amount: BigDecimal
)
