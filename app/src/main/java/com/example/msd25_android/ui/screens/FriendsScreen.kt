package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(onAddFriend: () -> Unit) {
    val friends = remember { listOf("Mille","Peter","Bastian","Nikolaj","Julius") }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("FRIENDS") }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onAddFriend, modifier = Modifier.fillMaxWidth()) { Text("ADD FRIEND") }
            friends.forEach { Text("• $it", style = MaterialTheme.typography.titleMedium) }
        }
    }
}
