package com.example.msd25_android.ui.screens

import android.app.Application
import android.icu.math.BigDecimal
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.data.models.Expense
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.logic.data.models.User
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.fold
import com.example.msd25_android.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    group: Group,
    onOpenDetails: () -> Unit,
    onBack: () -> Unit = {}
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val members = remember { mutableStateListOf<User>() }
    val expenses = remember { mutableStateListOf<Expense>() }
    var phone by remember { mutableStateOf("") }

    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showAddDialog) {
        coroutineScope.launch(Dispatchers.IO) {
            /*phone = userRepository.currentUserId.first()!!

            val memberRes = groupViewModel.getGroupWithMembers(group.id)
            if (memberRes.success) {
                members.clear()
                members.addAll(memberRes.data!!.members)
            }
            val expenseRes = groupViewModel.getGroupWithExpenses(group.id)
            if (expenseRes.success) {
                val ids = expenseRes.data!!.expenses.map { it.id }
                expenses.clear()
                val sharesRes = expenseViewModel.getExpensesWithShares(ids)
                if (sharesRes.success) {
                    expenses.clear()
                    expenses.addAll(sharesRes.data!!)
                }
            }*/
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(group.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Expense",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Box(Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Button(
                        onClick = { onOpenDetails() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("DETAILS") }
                }
            }
        }
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(expenses) { e ->
                    ExpenseBubble(expense = e)
                }
            }
        }
    }

    if (showAddDialog) {
        AddExpenseDialog(
            group = group,
            phone = phone,
            members = members,
            onDismiss = {
                showAddDialog = false

                        },
        )
    }
}

@Composable
private fun ExpenseBubble(expense: Expense) {

    val amount: BigDecimal = expense.expense_shares!!.fold(BigDecimal.ZERO) {acc, share ->
        acc.add(share.amount)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(
                    text = "${expense.paid_by_user!!.name} spent ${amount.format(-1, 2)} kr" +
                            if (expense.description!!.isNotBlank()) " on ${expense.description}" else "",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}



