package com.example.msd25_android.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.msd25_android.R
import com.example.msd25_android.logic.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: suspend (phone: String, password: String) -> AuthResponse,
    onGoToSignUp: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }

    var failedMsg by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(" ") }) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { inner ->
        val topPad = inner.calculateTopPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPad, start = 24.dp, end = 24.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
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

                Text(
                    text = failedMsg,
                    color = Color(217, 97, 97, 255)
                )

                Button(
                    onClick = {
                        Log.w("loginScreen", "onLogin triggered with $phone and $pwd")
                        coroutineScope.launch(Dispatchers.IO) {
                            val response = onLogin(phone, pwd)
                            if (!response.success) {
                            failedMsg = response.message
                        } }

                              },
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
}
