package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenGroup: () -> Unit,
    onCreateGroup: () -> Unit,
    onGoToFriends: () -> Unit
) {
    val groups = remember { listOf("Roomies", "Siblings", "Trip to Aarhus") }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("LOGO", fontWeight = FontWeight.Bold) }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onCreateGroup, modifier = Modifier.fillMaxWidth()) { Text("ADD GROUP") }
            Button(onClick = onOpenGroup, modifier = Modifier.fillMaxWidth()) { Text("OPEN ONGOING GROUP") }
            OutlinedButton(onClick = onGoToFriends, modifier = Modifier.fillMaxWidth()) { Text("GO TO FRIENDS") }
            Divider()
            Text("Your groups:")
            groups.forEach { Text("• $it") }
        }
    }
}
