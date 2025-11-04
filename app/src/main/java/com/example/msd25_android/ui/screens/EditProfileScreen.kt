package com.example.msd25_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
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

    val spacing = 14.dp

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("EDIT PROFILE") }) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { inner ->
        val topPad = inner.calculateTopPadding()
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = topPad)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { onPickImage?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "M",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
            }

            Spacer(Modifier.height(36.dp))

            LabeledInputCardBasic(
                label = "Name",
                value = name,
                onValueChange = { name = it },
                placeholder = "Enter your full name"
            )

            Spacer(Modifier.height(spacing))

            LabeledInputCardBasic(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "name@example.com"
            )

            Spacer(Modifier.height(spacing))

            LabeledInputCardBasic(
                label = "Phone",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "+45 12 34 56 78"
            )

            Spacer(Modifier.height(spacing))

            LabeledInputCardBasic(
                label = "Birthday",
                value = birthday,
                onValueChange = { birthday = it },
                placeholder = "YYYY-MM-DD",
                modifier = Modifier.clickable { }
            )

            Spacer(Modifier.height(spacing))

            LabeledInputCardBasic(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "••••••••",
                isPassword = true,
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword },
                smaller = true
            )

            Spacer(Modifier.height(spacing))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
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
                        Text(
                            text = "Notifications",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = if (notificationsEnabled) "Enabled" else "Disabled",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            }

            Spacer(Modifier.height(spacing))

            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(40.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
private fun LabeledInputCardBasic(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    smaller: Boolean = false
) {
    val textStyle =
        if (smaller) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = if (smaller) 8.dp else 10.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(if (smaller) 2.dp else 6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        textStyle = textStyle.merge(
                            TextStyle(color = MaterialTheme.colorScheme.onSurface)
                        ),
                        visualTransformation = if (isPassword && !showPassword)
                            PasswordVisualTransformation()
                        else
                            VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(modifier)
                    )
                }
                if (isPassword && onTogglePassword != null) {
                    TextButton(onClick = onTogglePassword) {
                        Text(
                            if (showPassword) "Hide" else "Show",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
