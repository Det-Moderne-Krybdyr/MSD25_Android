package com.example.msd25_android.ui.screens

import android.app.Application
import android.icu.math.BigDecimal
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.logic.data.models.User
import com.example.msd25_android.logic.services.GroupService
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    group: Group,
    onPay: (amount: BigDecimal) -> Unit,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    groupService: GroupService = viewModel()
) {
    val cs = MaterialTheme.colorScheme
    val balances = remember { mutableStateMapOf<Long, BigDecimal>() }
    var myBalance by remember { mutableStateOf(BigDecimal.ZERO) }
    val members = remember { mutableStateListOf<User>() }
    var userId by remember { mutableLongStateOf(0)}

    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)
    val coroutineScope = rememberCoroutineScope()

    suspend fun getData() {
        userId = userRepository.currentUserId.first()!!.toLong()
        groupService.getGroupInfo(group.id) { response ->
            if (response.success) {
                members.clear()
                members.addAll(response.data!!.members)
                // sadly nesting calls now
                members.forEach { member ->
                    coroutineScope.launch {
                        groupService.getPersonalBalance(member.id, group.id) { response ->
                            if (response.success) {
                                balances[member.id] = response.data!!
                                if (member.id == userId) myBalance = response.data
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            getData()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("${group.name} • Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            groupService.deleteGroup(group.id) {
                                onDelete()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Group",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Box(Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Button(
                        onClick = { onPay(myBalance) },
                        enabled = myBalance < BigDecimal.ZERO,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = cs.onPrimary,
                            disabledContainerColor = cs.surfaceVariant,
                            disabledContentColor = cs.onSurfaceVariant
                        )
                    ) {
                        Text(if (myBalance < BigDecimal.ZERO) "Pay Now" else "All Set")
                    }
                }
            }
        }
    ) { p ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(p)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(members) { member ->
                    if (member.id != userId )
                        MemberBalanceRow(name = member.name, balance = balances[member.id] ?: BigDecimal.ZERO)
                }
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = cs.surfaceVariant),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Your balance", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = myBalance.format(-1, 2),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = when {
                            myBalance > BigDecimal.ZERO -> cs.primary
                            myBalance < BigDecimal.ZERO -> cs.error
                            else -> cs.onSurface
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MemberBalanceRow(name: String, balance: BigDecimal) {
    val cs = MaterialTheme.colorScheme

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cs.surfaceVariant,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(cs.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.first().uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = cs.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium, color = cs.onSurface)
            }
            val text = balance.format(-1, 2)
            val color = when {
                balance > BigDecimal.ZERO -> cs.primary
                balance < BigDecimal.ZERO -> cs.error
                else -> cs.onSurfaceVariant
            }
            Text(text, color = color, style = MaterialTheme.typography.titleMedium)
        }
    }
}
