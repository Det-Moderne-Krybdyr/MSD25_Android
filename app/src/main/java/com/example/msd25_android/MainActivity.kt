package com.example.msd25_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.msd25_android.ui.screens.*
import com.example.msd25_android.ui.theme.MSD25_AndroidTheme
import kotlin.math.absoluteValue

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

@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun MSD25_AndroidApp() {
    var current by remember { mutableStateOf(AppDestinations.HOME) }
    val bottom = listOf(AppDestinations.FRIENDS, AppDestinations.HOME, AppDestinations.PROFILE)
    val cs = MaterialTheme.colorScheme

    var membersForDetails by remember { mutableStateOf(listOf<String>()) }
    var expensesForDetails by remember { mutableStateOf(listOf<Expense>()) }
    var currentUserForDetails by remember { mutableStateOf("") }
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
                    onOpenGroup = { current = AppDestinations.GROUP },
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
                    onDone = { current = AppDestinations.HOME }
                )
                AppDestinations.GROUP -> GroupScreen(
                    onOpenDetails = { members, expenses, currentUser ->
                        membersForDetails = members
                        expensesForDetails = expenses
                        currentUserForDetails = currentUser
                        current = AppDestinations.GROUP_DETAILS
                    }
                )
                AppDestinations.GROUP_DETAILS -> GroupDetailScreen(
                    members = membersForDetails,
                    expenses = expensesForDetails,
                    currentUser = currentUserForDetails,
                    onPay = {
                        val balances = membersForDetails.associateWith { 0.0 }.toMutableMap()
                        if (membersForDetails.isNotEmpty()) {
                            expensesForDetails.forEach { e ->
                                val share = e.amount / membersForDetails.size
                                membersForDetails.forEach { m -> balances[m] = (balances[m] ?: 0.0) - share }
                                balances[e.name] = (balances[e.name] ?: 0.0) + e.amount
                            }
                        }
                        amountForPay = (balances[currentUserForDetails] ?: 0.0).absoluteValue
                        current = AppDestinations.PAY
                    },
                    onBack = { current = AppDestinations.GROUP }
                )
                AppDestinations.PAY -> PayScreen(
                    amount = amountForPay,
                    onDone = { current = AppDestinations.GROUP_DETAILS }
                )
                AppDestinations.EDIT_PROFILE -> EditProfileScreen(
                    onDone = { current = AppDestinations.PROFILE }
                )
            }
        }
    }
}
