package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(onOpenDetails: () -> Unit) {
    val group = remember { "Roomies" }
    val members = remember { listOf("Mille","Julius","Peter") }
    val expenses = remember { 12 }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(group) }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Members: ${members.joinToString()}")
            Text("Expenses: $expenses")
            Button(onClick = onOpenDetails, modifier = Modifier.fillMaxWidth()) { Text("OPEN DETAILS") }
        }
    }
}
