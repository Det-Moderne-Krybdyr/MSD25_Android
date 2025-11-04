package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayScreen(onDone: () -> Unit) {
    var amount by remember { mutableStateOf("270.00") }
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("PAY") }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(amount, { amount = it }, label = { Text("Amount (DKK)") })
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("CONFIRM PAYMENT") }
        }
    }
}
