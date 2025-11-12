package com.example.msd25_android.ui.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.viewmodels.UserViewModel
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    onAddFriend: () -> Unit = {},  // handled by MainActivity
    userViewModel: UserViewModel = viewModel()
) {
    val cs = MaterialTheme.colorScheme

    val friends = remember { mutableStateListOf<User>() }
    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val phone = userRepository.currentPhoneNumber.first()
            val res = userViewModel.getUserWithFriends(phone!!)
            if (res.success) {
                friends.addAll(res.data!!.friends)
            }
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Friends") }) }
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
                .padding(16.dp)
        ) {
            Button(
                onClick = onAddFriend, // ✅ navigate to AddFriendScreen
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.primary,
                    contentColor = cs.onPrimary
                )
            ) {
                Text("Add Friend")
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(friends) { friend ->
                    FriendCard(name = friend.name)
                }
            }
        }
    }
}

@Composable
private fun FriendCard(name: String) {
    val cs = MaterialTheme.colorScheme

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cs.surfaceVariant,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(cs.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.first().uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = cs.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(text = name, style = MaterialTheme.typography.titleMedium, color = cs.onSurface)
        }
    }
}
