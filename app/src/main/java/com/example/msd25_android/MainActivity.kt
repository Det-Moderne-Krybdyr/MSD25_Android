package com.example.msd25_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.msd25_android.ui.screens.*
import com.example.msd25_android.ui.theme.MSD25_AndroidTheme
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MSD25_AndroidTheme(dynamicColor = false) {
                MSD25_AndroidApp()
            }
        }
    }
}

enum class AppDestinations(val label: String, val icon: ImageVector) {
    FRIENDS("Friends", Icons.Default.Person),
    HOME("Home", Icons.Default.Home),
    PROFILE("Profile", Icons.Default.AccountBox),
    LOGIN("Login", Icons.Default.Home),
    SIGNUP("SignUp", Icons.Default.Home),
    ADD_FRIEND("AddFriend", Icons.Default.Home),
    ADD_GROUP("AddGroup", Icons.Default.Home),
    GROUP("Group", Icons.Default.Home),
    GROUP_DETAILS("GroupDetails", Icons.Default.Home),
    PAY("Pay", Icons.Default.Home),
    EDIT_PROFILE("EditProfile", Icons.Default.Home)
}

data class GroupModel(
    val id: String,
    val name: String,
    val members: List<String>,
    val expenses: SnapshotStateList<Expense>
)

private fun myBalanceFor(user: String, members: List<String>, expenses: List<Expense>): Double {
    val balances = members.associateWith { 0.0 }.toMutableMap()
    if (members.isNotEmpty()) {
        expenses.forEach { e ->
            val share = e.amount / members.size
            members.forEach { m -> balances[m] = (balances[m] ?: 0.0) - share }
            balances[e.name] = (balances[e.name] ?: 0.0) + e.amount
        }
    }
    return balances[user] ?: 0.0
}

@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun MSD25_AndroidApp() {
    var current by remember { mutableStateOf(AppDestinations.HOME) }
    val bottom = listOf(AppDestinations.FRIENDS, AppDestinations.HOME, AppDestinations.PROFILE)
    val cs = MaterialTheme.colorScheme

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

    var selectedGroup by remember { mutableStateOf(roomies) }

    val summaries = listOf(roomies, siblings, trip).map { g ->
        GroupSummary(
            id = g.id,
            name = g.name,
            balanceDkk = myBalanceFor(currentUser, g.members, g.expenses).roundToInt()
        )
    }

    var amountForPay by remember { mutableStateOf(0.0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = cs.surface, contentColor = cs.onSurface) {
                bottom.forEach { d ->
                    NavigationBarItem(
                        selected = d == current,
                        onClick = { current = d },
                        icon = { Icon(d.icon, contentDescription = d.label) },
                        label = { Text(d.label) },
                        modifier = Modifier.height(56.dp),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = cs.primary,
                            selectedTextColor = cs.primary,
                            indicatorColor = cs.primary.copy(alpha = 0.15f),
                            unselectedIconColor = cs.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = cs.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (current) {
                AppDestinations.FRIENDS -> FriendsScreen(
                    onAddFriend = { current = AppDestinations.ADD_FRIEND }
                )
                AppDestinations.HOME -> HomeScreen(
                    groups = summaries,
                    onOpenGroup = { groupId ->
                        selectedGroup = when (groupId) {
                            roomies.id -> roomies
                            siblings.id -> siblings
                            trip.id -> trip
                            else -> roomies
                        }
                        current = AppDestinations.GROUP
                    },
                    onCreateGroup = { current = AppDestinations.ADD_GROUP },
                    onGoToFriends = { current = AppDestinations.FRIENDS }
                )
                AppDestinations.PROFILE -> ProfileScreen(
                    onEdit = { current = AppDestinations.EDIT_PROFILE },
                    onLogout = { current = AppDestinations.LOGIN }
                )
                AppDestinations.LOGIN -> LoginScreen(
                    onLogin = { current = AppDestinations.HOME },
                    onGoToSignUp = { current = AppDestinations.SIGNUP }
                )
                AppDestinations.SIGNUP -> SignUpScreen(
                    onCreate = { current = AppDestinations.HOME },
                    onGoToLogin = { current = AppDestinations.LOGIN }
                )
                AppDestinations.ADD_FRIEND -> AddFriendScreen(
                    onDone = { current = AppDestinations.FRIENDS }
                )
                AppDestinations.ADD_GROUP -> CreateGroupScreen(
                    onDone = { current = AppDestinations.HOME },
                    onBack = { current = AppDestinations.HOME }
                )
                AppDestinations.GROUP -> GroupScreen(
                    groupName = selectedGroup.name,
                    members = selectedGroup.members,
                    currentUser = currentUser,
                    expenses = selectedGroup.expenses,
                    onOpenDetails = { _, _, _ ->
                        current = AppDestinations.GROUP_DETAILS
                    },
                    onBack = { current = AppDestinations.HOME }
                )
                AppDestinations.GROUP_DETAILS -> GroupDetailScreen(
                    groupName = selectedGroup.name,
                    members = selectedGroup.members,
                    expenses = selectedGroup.expenses,
                    currentUser = currentUser,
                    onPay = {
                        amountForPay = myBalanceFor(currentUser, selectedGroup.members, selectedGroup.expenses)
                            .absoluteValue
                        current = AppDestinations.PAY
                    },
                    onBack = { current = AppDestinations.GROUP }
                )
                AppDestinations.PAY -> PayScreen(
                    amount = amountForPay,
                    onDone = { current = AppDestinations.GROUP_DETAILS },
                    onBack = { current = AppDestinations.GROUP_DETAILS }
                )
                AppDestinations.EDIT_PROFILE -> EditProfileScreen(
                    onDone = { current = AppDestinations.PROFILE }
                )
            }
        }
    }
}
