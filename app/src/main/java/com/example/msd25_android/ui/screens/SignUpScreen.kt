package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onCreate: () -> Unit,
    onGoToLogin: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var birthday by rememberSaveable { mutableStateOf("") }
    var pw by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var accepted by rememberSaveable { mutableStateOf(false) }

    val canCreate = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
            birthday.isNotBlank() && pw.isNotBlank() && confirm.isNotBlank() &&
            pw == confirm && accepted

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("LOGO") }) }) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(email, { email = it }, label = { Text("E-mail") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(phone, { phone = it }, label = { Text("Phone") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(birthday, { birthday = it }, label = { Text("Birthday") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(pw, { pw = it }, label = { Text("Password") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(confirm, { confirm = it }, label = { Text("Confirm Password") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = confirm.isNotEmpty() && pw != confirm,
                supportingText = { if (confirm.isNotEmpty() && pw != confirm) Text("Passwords don’t match") },
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                Checkbox(accepted, { accepted = it })
                Spacer(Modifier.width(8.dp))
                Text("Accept terms & conditions")
            }
            Button(onClick = onCreate, enabled = canCreate, modifier = Modifier.fillMaxWidth()) {
                Text("Create User")
            }
            TextButton(onClick = onGoToLogin, modifier = Modifier.fillMaxWidth()) {
                Text("Already a user?")
            }
        }
    }
}
