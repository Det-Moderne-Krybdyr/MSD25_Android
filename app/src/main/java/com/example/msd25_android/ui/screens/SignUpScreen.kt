package com.example.msd25_android.ui.screens

import CustomDatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.msd25_android.R
import com.example.msd25_android.logic.BackendResponse
import com.example.msd25_android.logic.data.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onCreate: suspend (user: User) -> BackendResponse<Unit>,
    onGoToLogin: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var pw by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var accepted by rememberSaveable { mutableStateOf(false) }

    val canCreate = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
            pw.isNotBlank() && confirm.isNotBlank() &&
            pw == confirm && accepted

    val coroutineScope = rememberCoroutineScope()
    var failedMsg by remember { mutableStateOf("") }

    val initialDateMillis = LocalDate(2025, 1, 15)
        .atStartOfDayIn(TimeZone.UTC)
        .toEpochMilliseconds()

    val datePickerState = rememberDatePickerState(
        //initialSelectedDateMillis = initialDateMillis,
        initialDisplayMode = DisplayMode.Picker,
    )

    val cs = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo2),
                        contentDescription = "FairShare logo",
                        modifier = Modifier
                            .width(160.dp)
                            .padding(vertical = 6.dp)
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            CustomDatePicker(state = datePickerState)

            OutlinedTextField(
                value = pw,
                onValueChange = { pw = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirm,
                onValueChange = { confirm = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = confirm.isNotEmpty() && pw != confirm,
                supportingText = {
                    if (confirm.isNotEmpty() && pw != confirm)
                        Text("Passwords don’t match")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = failedMsg,
                color = cs.error
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = accepted, onCheckedChange = { accepted = it })
                Spacer(Modifier.width(8.dp))
                Text("Accept terms & conditions")
            }

            Button(
                onClick = {
                    val user = User(
                        name = name,
                        email = email,
                        phoneNumber = phone,
                        password = pw,
                        birthdate = Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis!!)
                    )
                    coroutineScope.launch(Dispatchers.IO) {
                        val response = onCreate(user)
                        if (!response.success) failedMsg = response.message
                    }
                },
                enabled = canCreate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create User")
            }

            TextButton(
                onClick = onGoToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Already a user?")
            }
        }
    }
}
