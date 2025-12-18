package com.example.msd25_android.ui.nav

import android.icu.math.BigDecimal
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.msd25_android.AppDestinations
import com.example.msd25_android.logic.SessionManager
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.ui.screens.AddFriendScreen
import com.example.msd25_android.ui.screens.CreateGroupScreen
import com.example.msd25_android.ui.screens.EditProfileScreen
import com.example.msd25_android.ui.screens.FriendsScreen
import com.example.msd25_android.ui.screens.GroupDetailScreen
import com.example.msd25_android.ui.screens.GroupScreen
import com.example.msd25_android.ui.screens.HomeScreen
import com.example.msd25_android.ui.screens.PayScreen
import com.example.msd25_android.ui.screens.ProfileScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeNav(current: AppDestinations, setCurrent: (AppDestinations) -> Unit, sessionManager: SessionManager) {

    val coroutineScope = rememberCoroutineScope()

    var selectedGroup by remember { mutableStateOf<Group?>(null) }

    var amountForPay by remember { mutableStateOf(BigDecimal.ZERO) }


    return when (current) {
        AppDestinations.FRIENDS -> FriendsScreen(
            onAddFriend = { setCurrent(AppDestinations.ADD_FRIEND) }
        )
        AppDestinations.HOME -> HomeScreen(
            onOpenGroup = { group ->
                selectedGroup = group
                setCurrent(AppDestinations.GROUP)
            },
            onCreateGroup = { setCurrent(AppDestinations.ADD_GROUP) }
        )
        AppDestinations.PROFILE -> ProfileScreen(
            onEdit = { setCurrent(AppDestinations.EDIT_PROFILE) },
            onLogout = {
                coroutineScope.launch(Dispatchers.IO) { sessionManager.logout() }
                setCurrent(AppDestinations.HOME)
            }
        )
        AppDestinations.ADD_FRIEND -> AddFriendScreen(
            onDone = { setCurrent(AppDestinations.FRIENDS) }
        )
        AppDestinations.ADD_GROUP -> CreateGroupScreen(
            onDone = { setCurrent(AppDestinations.HOME) },
            onBack = { setCurrent(AppDestinations.HOME) }
        )
        AppDestinations.GROUP -> GroupScreen(
            group = selectedGroup!!,
            onOpenDetails = {
                setCurrent(AppDestinations.GROUP_DETAILS)
            },
            onBack = { setCurrent(AppDestinations.HOME) }
        )
        AppDestinations.GROUP_DETAILS -> GroupDetailScreen(
            group = selectedGroup!!,
            onPay = { amount ->
                amountForPay = amount
                setCurrent(AppDestinations.PAY)
            },
            onBack = { setCurrent(AppDestinations.GROUP) }
        )
        AppDestinations.PAY -> PayScreen(
            amount = amountForPay,
            group = selectedGroup!!,
            onDone = { setCurrent(AppDestinations.GROUP_DETAILS) },
            onBack = { setCurrent(AppDestinations.GROUP_DETAILS) }
        )
        AppDestinations.EDIT_PROFILE -> EditProfileScreen(
            onDone = { setCurrent(AppDestinations.PROFILE) }
        )
    }
}