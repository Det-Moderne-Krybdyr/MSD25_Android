package com.example.msd25_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.msd25_android.ui.screens.*
import com.example.msd25_android.ui.theme.MSD25_AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MSD25_AndroidTheme { MSD25_AndroidApp() } }
    }
}

enum class AppDestinations(val label: String, val icon: ImageVector) {
    // Bottom nav
    FRIENDS("Friends", Icons.Default.Person),
    HOME("Home", Icons.Default.Home),
    PROFILE("Profile", Icons.Default.AccountBox),

    // “Skjulte” visninger (tilgås via knapper)
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
@Composable
fun MSD25_AndroidApp() {
    var current by remember { mutableStateOf(AppDestinations.HOME) }

    val bottom = listOf(AppDestinations.FRIENDS, AppDestinations.HOME, AppDestinations.PROFILE)

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            bottom.forEach { d ->
                item(
                    icon = { Icon(d.icon, contentDescription = d.label) },
                    label = { Text(d.label) },
                    selected = d == current,
                    onClick = { current = d }
                )
            }
        }
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
                onEdit = { current = AppDestinations.EDIT_PROFILE }
            )

            // “Skjulte” sider
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
                onDone = { current = AppDestinations.PROFILE }
            )
        }
    }
}
