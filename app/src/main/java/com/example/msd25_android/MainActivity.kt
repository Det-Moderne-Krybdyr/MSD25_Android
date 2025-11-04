package com.example.msd25_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.msd25_android.ui.screens.*
import com.example.msd25_android.ui.theme.MSD25_AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MSD25_AndroidTheme(dynamicColor = false) {   // tving dine grønne farver
                MSD25_AndroidApp()
            }
        }
    }
}

enum class AppDestinations(val label: String, val icon: ImageVector) {
    FRIENDS("Friends", Icons.Default.Person),
    HOME("Home", Icons.Default.Home),
    PROFILE("Profile", Icons.Default.AccountBox),

    // “skjulte” sider du kan navigere til via knapper
    LOGIN("Login", Icons.Default.Home),
    SIGNUP("SignUp", Icons.Default.Home),
    ADD_FRIEND("AddFriend", Icons.Default.Home),
    ADD_GROUP("AddGroup", Icons.Default.Home),
    GROUP("Group", Icons.Default.Home),
    GROUP_DETAILS("GroupDetails", Icons.Default.Home),
    PAY("Pay", Icons.Default.Home),
    EDIT_PROFILE("EditProfile", Icons.Default.Home),
}

@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun MSD25_AndroidApp() {
    var current by remember { mutableStateOf(AppDestinations.HOME) }
    val bottom = listOf(AppDestinations.FRIENDS, AppDestinations.HOME, AppDestinations.PROFILE)
    val cs = MaterialTheme.colorScheme

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = cs.surface,           // hvid bund
                contentColor = cs.onSurface
            ) {
                bottom.forEach { d ->
                    NavigationBarItem(
                        selected = d == current,
                        onClick = { current = d },
                        icon = { Icon(d.icon, contentDescription = d.label) },
                        label = { Text(d.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = cs.primary,                   // grøn highlight
                            selectedTextColor   = cs.primary,
                            indicatorColor      = cs.primary.copy(alpha = 0.15f),
                            unselectedIconColor = cs.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = cs.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
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

                // “skjulte” ruter
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
                    onOpenDetails = { current = AppDestinations.GROUP_DETAILS }
                )
                AppDestinations.GROUP_DETAILS -> GroupDetailScreen(
                    onPay = { current = AppDestinations.PAY },
                    onBack = { current = AppDestinations.GROUP }
                )
                AppDestinations.PAY -> PayScreen(
                    onDone = { current = AppDestinations.GROUP_DETAILS }
                )
                AppDestinations.EDIT_PROFILE -> EditProfileScreen(
                    onDone = { current = AppDestinations.PROFILE },
                )
            }
        }
    }
}
