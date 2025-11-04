package com.example.msd25_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onDone: () -> Unit,
    onPickImage: (() -> Unit)? = null
) {
    var name by rememberSaveable { mutableStateOf("Mille Nordal Jakobsen") }
    var email by rememberSaveable { mutableStateOf("mille@example.com") }
    var phone by rememberSaveable { mutableStateOf("+45 12 34 56 78") }
    var birthday by rememberSaveable { mutableStateOf("1999-05-12") }
    var password by rememberSaveable { mutableStateOf("••••••••") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

    val cs = MaterialTheme.colorScheme
    val scroll = rememberScrollState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("EDIT PROFILE") }) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { inner ->
        val topPad = inner.calculateTopPadding()
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = topPad)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(cs.primaryContainer)
                    .clickable { onPickImage?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "M",
                    style = MaterialTheme.typography.headlineLarge,
                    color = cs.onPrimaryContainer
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(cs.secondaryContainer)
                )
            }

            Spacer(Modifier.height(28.dp))

            // --- Felter i samme stil som Login (OutlinedTextField) ---
            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = cs.primary,
                unfocusedBorderColor = cs.primary.copy(alpha = 0.5f),
                focusedLabelColor = cs.primary,
                unfocusedLabelColor = cs.onSurfaceVariant
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = birthday,
                onValueChange = { birthday = it },
                label = { Text("Birthday") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { showPassword = !showPassword }) {
                        Text(if (showPassword) "Hide" else "Show", style = MaterialTheme.typography.labelSmall)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            Spacer(Modifier.height(16.dp))

            // Notifications “card” — mere neutral, samme sprog som øvrige skærme
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = cs.surfaceVariant,
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Notifications", style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)
                        Spacer(Modifier.height(2.dp))
                        Text(if (notificationsEnabled) "Enabled" else "Disabled", style = MaterialTheme.typography.bodyLarge, color = cs.onSurface)
                    }
                    Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save")
            }
        }
    }
}
