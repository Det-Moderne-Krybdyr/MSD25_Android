package com.example.msd25_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.msd25_android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onGoToSignUp: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(" ") }) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { inner ->
        val topPad = inner.calculateTopPadding()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPad, start = 24.dp, end = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "FairShare logo",
                modifier = Modifier
                    .width(260.dp)
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pwd,
                onValueChange = { pwd = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Login")
            }

            TextButton(
                onClick = onGoToSignUp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Not a user yet?")
            }
        }
    }
}
