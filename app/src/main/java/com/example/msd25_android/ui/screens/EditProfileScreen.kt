package com.example.msd25_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
    onPickImage: (() -> Unit)? = null // wire this up later to an image picker
) {
    var name by rememberSaveable { mutableStateOf("Mille Nordal Jakobsen") }
    var email by rememberSaveable { mutableStateOf("mille@example.com") }
    var phone by rememberSaveable { mutableStateOf("+45 12 34 56 78") }
    var birthday by rememberSaveable { mutableStateOf("1999-05-12") } // mock until DB
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("EDIT PROFILE") }) }
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Smaller profile picture than ProfileScreen + clickable to change
            Box(
                modifier = Modifier
                    .size(120.dp) // smaller than the large avatar on ProfileScreen
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { onPickImage?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                // Use initial as placeholder (keeps parity with ProfileScreen look)
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "M",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                // Tiny edit badge in the corner
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
            }

            // ⬇️ Keep the same visual rhythm as ProfileScreen
            Spacer(modifier = Modifier.height(56.dp))

            // Editable “cards” that mirror InfoCard styling
            EditableCard(label = "Name") {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your full name") }
                )
            }

            Spacer(Modifier.height(20.dp))

            EditableCard(label = "Email") {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("name@example.com") }
                )
            }

            Spacer(Modifier.height(20.dp))

            EditableCard(label = "Phone") {
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("+45 12 34 56 78") }
                )
            }

            Spacer(Modifier.height(20.dp))

            EditableCard(label = "Birthday") {
                TextField(
                    value = birthday,
                    onValueChange = { birthday = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* later: open date picker */ },
                    placeholder = { Text("YYYY-MM-DD") }
                )
            }

            Spacer(Modifier.height(20.dp))

            EditableCard(label = "Password") {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter a new password") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(if (showPassword) "Hide" else "Show")
                        }
                    }
                )
            }

            Spacer(Modifier.height(20.dp))

            // Notifications setting in the same “card” style
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "Allow notifications",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (notificationsEnabled) "Enabled" else "Disabled",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    // Using Switch (platform-standard for a boolean). If you truly want a slider, we can force a 0–1 Slider.
                    Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("SAVE")
            }
        }
    }
}

// Re-usable card that matches your InfoCard visuals but hosts editable content
@Composable
private fun EditableCard(
    label: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            content()
        }
    }
}
