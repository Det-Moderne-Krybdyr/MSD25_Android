package com.example.msd25_android.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.viewmodels.UserViewModel
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    onDone: () -> Unit,
    userViewModel: UserViewModel = viewModel(),
) {

    var friendPhoneNumber by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Add Friend") }) }
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Add a new friend by entering their phone number:",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = friendPhoneNumber,
                onValueChange = { friendPhoneNumber = it },
                label = { Text("Phone number") },
                placeholder = { Text("e.g. +45 1234 5678") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error
            )

            Button(
                onClick = {
                    if (friendPhoneNumber.isNotBlank()) {

                        coroutineScope.launch(Dispatchers.IO) {
                            val res = userViewModel.getUserByPhone(friendPhoneNumber)
                            val phone = userRepository.currentPhoneNumber.first()
                            if (phone == friendPhoneNumber) {
                                errorMsg = "Cannot add yourself"
                                return@launch
                            }
                            if (!res.success || phone == null) {
                                errorMsg = "No user found"
                                return@launch
                            }
                            val friend = res.data!!
                            // check if users are already friends
                            val friends = userViewModel.getUserWithFriends(phone).data!!.friends
                            if (friends.filter { it.phoneNumber == friendPhoneNumber }.size == 1) {
                                errorMsg = "You are already friends with this user"
                                return@launch
                            }

                            val user = userViewModel.getUserByPhone(phone).data!!

                            userViewModel.createFriendship(user.id, friend.id)
                            onDone()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Friend")
            }
        }
    }
}
