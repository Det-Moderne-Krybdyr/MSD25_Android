package com.example.msd25_android.ui.screens

import android.app.Application
import android.icu.math.BigDecimal
import android.icu.math.MathContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.data.expense.ExpenseWithShares
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.viewmodels.GroupViewModel
import com.example.msd25_android.logic.viewmodels.ExpenseViewModel
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.collections.fold
import com.example.msd25_android.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    groupViewModel: GroupViewModel = viewModel(),
    expenseViewModel: ExpenseViewModel = viewModel(),
    group: Group,
    onOpenDetails: () -> Unit,
    onBack: () -> Unit = {}
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val members = remember { mutableStateListOf<User>() }
    val expenses = remember { mutableStateListOf<ExpenseWithShares>() }
    var phone by remember { mutableStateOf("") }

    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showAddDialog) {
        coroutineScope.launch(Dispatchers.IO) {
            phone = userRepository.currentPhoneNumber.first()!!

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
            }
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
private fun ExpenseBubble(expense: ExpenseWithShares) {

    val amount: BigDecimal = expense.shares.fold(BigDecimal.ZERO) {acc, share ->
        acc.add(share.amountOwed)
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
                    text = "${expense.user.name} spent ${amount.format(-1, 2)} kr" +
                            if (expense.expense.description.isNotBlank()) " on ${expense.expense.description}" else "",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}



