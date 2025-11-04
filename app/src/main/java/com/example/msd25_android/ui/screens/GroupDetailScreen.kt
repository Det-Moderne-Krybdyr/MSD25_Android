package com.example.msd25_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(onPay: () -> Unit, onBack: () -> Unit) {
    val balances = remember {
        listOf("+500 – Mille", "+268 – Julius", "+312 – Peter")
    }
    val total = remember { -270.0 }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("GROUP DETAILS") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            balances.forEach { Text(it) }
            ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("Balance"); Text("${"%.2f".format(total)} kr") } }
            Button(onClick = onPay, modifier = Modifier.fillMaxWidth()) { Text("PAY NOW") }
        }
    }
}
