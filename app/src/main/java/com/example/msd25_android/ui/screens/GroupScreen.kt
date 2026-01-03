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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.data.models.Expense
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.logic.data.models.User
import com.example.msd25_android.logic.services.GroupService
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.fold
import com.example.msd25_android.ui.components.*
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    group: Group,
    onOpenDetails: () -> Unit,
    onBack: () -> Unit = {},
    groupService: GroupService = viewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val members = remember { mutableStateListOf<User>() }
    val expenses = remember { mutableStateListOf<Expense>() }
    var userId by remember { mutableIntStateOf(0) }

    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showAddDialog) {
        coroutineScope.launch(Dispatchers.IO) {
            groupService.getExpenses(group.id) { response ->
                if (response.success) {
                    expenses.clear()
                    expenses.addAll(response.data!!)
                }
            }

            groupService.getGroupInfo(group.id) { response ->
                if (response.success) {
                    members.clear()
                    members.addAll(response.data!!.members)
                }
            }

            userId = userRepository.currentUserId.first()!!
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

    if (showAddDialog && members.isNotEmpty()) { // we must have retrieved members first
        AddExpenseDialog(
            group = group,
            userId = userId.toLong(),
            members = members,
            onDismiss = {
                showAddDialog = false
                        },
        )
    }
}

@Composable
private fun ExpenseBubble(expense: Expense) {

    val amount: BigDecimal = expense.expense_shares.fold(BigDecimal.ZERO) {acc, share ->
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
            Row(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                var leftHeightPx by remember { mutableIntStateOf(0) }
                val density = LocalDensity.current

                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .onGloballyPositioned { coords ->
                            leftHeightPx = coords.size.height
                        }
                ) {
                    Text(
                        text = expense.paid_by_user.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = amount.format(-1, 2),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = expense.description,
                        style = MaterialTheme .typography.titleSmall
                    )
                }

                val leftHeightDp = with(density) { leftHeightPx.toDp() }

                LazyColumn(
                    modifier = Modifier
                        .padding(12.dp)
                        .heightIn(max = leftHeightDp)
                ) {
                    items(expense.expense_shares) { share ->
                        Text(
                            text = "${share.user.name.split(" ")[0]} - ${share.amount.format(-1, 2)}"
                        )
                    }
                }
            }
        }
    }
}



