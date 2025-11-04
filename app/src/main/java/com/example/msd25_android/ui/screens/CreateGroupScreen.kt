package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(onDone: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var member by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf<String>() }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Add Group") }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(name, { name = it }, label = { Text("Group name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(member, { member = it }, label = { Text("Add member") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = { if (member.isNotBlank()) { members.add(member); member = "" } }) { Text("Add member") }
            Text("Members: ${members.joinToString()}")
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("Create Group") }
        }
    }
}
