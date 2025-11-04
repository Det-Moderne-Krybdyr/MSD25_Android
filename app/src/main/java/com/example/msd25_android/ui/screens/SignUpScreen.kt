package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(onCreate: () -> Unit, onGoToLogin: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("CREATE USER") }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(name, { name = it }, label = { Text("Name") })
            OutlinedTextField(email, { email = it }, label = { Text("E-mail") })
            OutlinedTextField(phone, { phone = it }, label = { Text("Phone") })
            OutlinedTextField(birthday, { birthday = it }, label = { Text("Birthday") })
            OutlinedTextField(pw, { pw = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
            OutlinedTextField(confirm, { confirm = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation())
            Row { Checkbox(accepted, { accepted = it }); Text("Accept terms & conditions") }
            Button(onClick = onCreate, enabled = accepted, modifier = Modifier.fillMaxWidth()) { Text("CREATE USER") }
            TextButton(onClick = onGoToLogin, modifier = Modifier.fillMaxWidth()) { Text("Already a user?") }
        }
    }
}
