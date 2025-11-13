package com.example.msd25_android.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.msd25_android.AppDestinations
import com.example.msd25_android.GroupModel
import com.example.msd25_android.logic.SessionManager
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.myBalanceFor
import com.example.msd25_android.ui.screens.AddFriendScreen
import com.example.msd25_android.ui.screens.CreateGroupScreen
import com.example.msd25_android.ui.screens.EditProfileScreen
import com.example.msd25_android.ui.screens.FriendsScreen
import com.example.msd25_android.ui.screens.GroupDetailScreen
import com.example.msd25_android.ui.screens.GroupScreen
import com.example.msd25_android.ui.screens.GroupSummary
import com.example.msd25_android.ui.screens.HomeScreen
import com.example.msd25_android.ui.screens.PayScreen
import com.example.msd25_android.ui.screens.ProfileScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun HomeNav(current: AppDestinations, setCurrent: (AppDestinations) -> Unit, sessionManager: SessionManager) {

    val coroutineScope = rememberCoroutineScope()
    val currentUser = remember { "Mille" }

    val roomies = remember {
        GroupModel(
            id = "roomies",
            name = "Roomies",
            members = listOf("Mille", "Julius", "Peter"),
            expenses = mutableStateListOf()
        )
    }
    val siblings = remember {
        GroupModel(
            id = "siblings",
            name = "Siblings",
            members = listOf("Mille", "Anna"),
            expenses = mutableStateListOf()
        )
    }
    val trip = remember {
        GroupModel(
            id = "trip-aarhus",
            name = "Trip to Aarhus",
            members = listOf("Mille", "Julius", "Peter", "Anna"),
            expenses = mutableStateListOf()
        )
    }

    var selectedGroup by remember { mutableStateOf<Group?>(null) }

    val summaries = listOf(roomies, siblings, trip).map { g ->
        GroupSummary(
            id = g.id,
            name = g.name,
            balanceDkk = myBalanceFor(currentUser, g.members, g.expenses).roundToInt()
        )
    }

    var amountForPay by remember { mutableDoubleStateOf(0.0) }


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
        AppDestinations.GROUP_DETAILS -> /*GroupDetailScreen(
            groupName = selectedGroup.name,
            members = selectedGroup.members,
            expenses = selectedGroup.expenses,
            currentUser = currentUser,
            onPay = {
                amountForPay = myBalanceFor(currentUser, selectedGroup.members, selectedGroup.expenses)
                    .absoluteValue
                setCurrent(AppDestinations.PAY)
            },
            onBack = { setCurrent(AppDestinations.GROUP) }
        )*/ {}
        AppDestinations.PAY -> PayScreen(
            amount = amountForPay,
            onDone = { setCurrent(AppDestinations.GROUP_DETAILS) },
            onBack = { setCurrent(AppDestinations.GROUP_DETAILS) }
        )
        AppDestinations.EDIT_PROFILE -> EditProfileScreen(
            onDone = { setCurrent(AppDestinations.PROFILE) }
        )
    }
}