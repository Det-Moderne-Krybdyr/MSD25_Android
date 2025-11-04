package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(onDone: () -> Unit) {
    var name by remember { mutableStateOf("Mille Nordal Jakobsen") }
    var email by remember { mutableStateOf("mille@example.com") }
    var phone by remember { mutableStateOf("+45 12 34 56 78") }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("EDIT PROFILE") }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(name, { name = it }, label = { Text("Name") })
            OutlinedTextField(email, { email = it }, label = { Text("E-mail") })
            OutlinedTextField(phone, { phone = it }, label = { Text("Phone") })
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("SAVE") }
        }
    }
}
