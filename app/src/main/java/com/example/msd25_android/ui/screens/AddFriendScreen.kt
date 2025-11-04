package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(onDone: () -> Unit) {
    var name by remember { mutableStateOf("") }
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("ADD FRIEND") }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(name, { name = it }, label = { Text("Friend name") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("SAVE") }
        }
    }
}
