package com.example.msd25_android.ui.screens

import CustomDatePicker
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.logic.data.models.User
import com.example.msd25_android.logic.services.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onDone: () -> Unit,
    onPickImage: (() -> Unit)? = null,
    userService: UserService = viewModel()
    ) {
    var user: User by remember { mutableStateOf(User()) }
    var name: String by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

    val cs = MaterialTheme.colorScheme
    val scroll = rememberScrollState()

    val initialDateMillis = LocalDate(2025, 1, 15)
        .atStartOfDayIn(TimeZone.UTC)
        .toEpochMilliseconds()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        initialDisplayMode = DisplayMode.Picker,
    )

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            userService.getUserInfo { res ->
                if (res.success) {
                    user = res.data!!
                    name = user.name
                    datePickerState.selectedDateMillis = user.birthdate
                }
            }
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Edit Profile") }) },
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

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(cs.primaryContainer)
                    .clickable { onPickImage?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.firstOrNull()?.uppercase() ?: "M",
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

            InfoCard(label = "Email", value = user.email)

            Spacer(Modifier.height(12.dp))

            InfoCard(label = "Phone", value = user.phone_number)

            Spacer(Modifier.height(12.dp))

            CustomDatePicker(state = datePickerState, borderColor = fieldColors.unfocusedIndicatorColor)

            Spacer(Modifier.height(12.dp))

            Spacer(Modifier.height(16.dp))

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
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val newUser = user.copy(
                            name = name,
                            birthdate = datePickerState.selectedDateMillis!!
                        )
                        userService.updateUser(newUser) {}
                    }
                    onDone()
                          },
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
