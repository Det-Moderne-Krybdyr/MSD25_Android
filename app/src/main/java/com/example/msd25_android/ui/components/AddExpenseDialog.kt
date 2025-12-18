package com.example.msd25_android.ui.components

import android.icu.math.BigDecimal
import android.icu.math.MathContext
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.logic.data.models.Expense
import com.example.msd25_android.logic.data.models.ExpenseShare
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.logic.data.models.User
import com.example.msd25_android.logic.services.GroupService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    group: Group,
    userId: Long,
    members: List<User>,
    onDismiss: () -> Unit,
    groupService: GroupService = viewModel()
) {
    var paidBy by remember { mutableStateOf(members.first() { it.id == userId }) }
    var amountText by remember { mutableStateOf("0") }
    var note by remember { mutableStateOf("") }
    val amountIsValid = amountText.toDoubleOrNull()?.let { it >= 0.0 } == true
    var expanded by remember { mutableStateOf(false) }

    val expenseShareCardData = remember { mutableStateListOf<MutableState<ExpenseShareCardData>>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        expenseShareCardData.addAll(members.map { mutableStateOf(ExpenseShareCardData(it)) })
    }

    val mc = MathContext(100, MathContext.PLAIN, false, MathContext.ROUND_HALF_DOWN)

    val calculateShares = {
        val fullAmount = BigDecimal(amountText.ifBlank { "0" })
        var splitAmount = fullAmount
        expenseShareCardData.forEach {
            if (it.value.isChecked && it.value.isChanged) {
                splitAmount = splitAmount.subtract(it.value.amount, mc)
            }
        }
        val numShares = expenseShareCardData.filter { it.value.isChecked && !it.value.isChanged }.size
        val shareAmount = if (numShares > 0) splitAmount.divide(BigDecimal(numShares), mc) else BigDecimal.ZERO
        expenseShareCardData.forEach {
            if (it.value.isChecked && !it.value.isChanged) {
                it.value = it.value.copy(amount = shareAmount)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = paidBy.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Who paid?") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Open menu"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        members.forEach { member ->
                            DropdownMenuItem(
                                text = { Text(member.name) },
                                onClick = {
                                    paidBy = member
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { change ->
                        amountText = change
                            .filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }
                            .replace(',', '.')
                        expenseShareCardData.forEach { it.value = it.value.copy(isChanged = false) }
                        calculateShares()
                    },
                    label = { Text("Amount (kr)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (what for?)") }
                )
                LazyColumn(
                    //verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f, fill = true)
                ) {
                    items(expenseShareCardData, key = { it.value.user.id }) { e ->
                        ExpenseShareCard(e, calculateShares)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = amountIsValid && note.isNotBlank(),
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {

                        val shares: MutableList<ExpenseShare> = mutableListOf()
                        expenseShareCardData.forEach {
                            if (it.value.isChecked && it.value.amount.compareTo(BigDecimal.ZERO) == 1) {
                                shares.add(ExpenseShare(
                                    user_id = it.value.user.id,
                                    amount = it.value.amount,
                                ))
                            }
                        }
                        val expense = Expense(
                            description = note,
                            paid_by_id = paidBy.id,
                            group_id = group.id,
                            expense_shares = shares
                        )

                        groupService.postExpense(expense) {onDismiss()}
                    }
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

data class ExpenseShareCardData(
    val user: User,
    val amount: BigDecimal = BigDecimal.ZERO,
    val isChecked: Boolean = true,
    val isChanged: Boolean = false
)

@Composable
fun ExpenseShareCard(data: MutableState<ExpenseShareCardData>, calculateCallback: () -> Unit) {

    val cs = MaterialTheme.colorScheme

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = cs.primary,
        unfocusedBorderColor = cs.primary.copy(alpha = 0.5f),
        focusedLabelColor = cs.primary,
        unfocusedLabelColor = cs.onSurfaceVariant
    )

    var amountText by remember { mutableStateOf(data.value.amount.format(-1, 2).replace(Regex("\\.?0+$"), "")) }

    // Sync when parent changes
    LaunchedEffect(data.value.amount) {
        if (data.value.isChanged && data.value.amount == BigDecimal.ZERO) {
            amountText = ""
        } else {
            amountText = data.value.amount.format(-1, 2).replace(Regex("\\.?0+$"), "")
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(14.dp)
    ) {
        Column {
            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                        .filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }
                        .replace(',', '.')
                    data.value = data.value.copy(isChanged = true)
                    var amount = BigDecimal.ZERO
                    if (amountText.isNotBlank()) {
                        amount = BigDecimal(amountText)
                    }
                    data.value = data.value.copy(amount = amount)
                    calculateCallback()
                },
                label = { Text(data.value.user.name) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = data.value.isChecked,
                colors = fieldColors
            )
        }
        Spacer(Modifier.weight(1f))
        Checkbox(
            checked = data.value.isChecked,
            onCheckedChange = {
                data.value = data.value.copy(
                    isChecked = it,
                    isChanged = false,
                    amount = BigDecimal.ZERO
                )
                calculateCallback()
            }
        )
    }


}