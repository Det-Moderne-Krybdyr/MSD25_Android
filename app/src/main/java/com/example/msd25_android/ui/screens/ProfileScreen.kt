package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onEdit: () -> Unit) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("PROFILE") }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Name: Mille Nordal Jakobsen")
            Text("Email: mille@example.com")
            Text("Phone: +45 12 34 56 78")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) { Text("EDIT / SETTINGS") }
        }
    }
}
