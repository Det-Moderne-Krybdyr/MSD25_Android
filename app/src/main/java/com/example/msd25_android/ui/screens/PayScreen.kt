package com.example.msd25_android.ui.screens

import android.app.Application
import android.icu.math.BigDecimal
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.R
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayScreen(
    amount: BigDecimal,
    group: Group,
    onDone: () -> Unit,
    onBack: () -> Unit,
) {
    val cs = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()
    var phone by remember { mutableStateOf("") }
    val userRepository = UserRepository((LocalContext.current.applicationContext as Application).dataStore)

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            /*phone = userRepository.currentUserId.first()!!
            // get group members
            val res = groupViewModel.getGroupWithMembers(group.id)
            if (res.success) {
                res.data!!.members.forEach { member ->
                    if (member.phoneNumber == phone) return@forEach
                    val balanceRes = expenseViewModel.getBalanceBetweenUsers(group.id, phone, member.id)
                    if (!balanceRes.success) return@forEach
                    if (balanceRes.data!!.amount > BigDecimal.ZERO) {
                        debts.add(balanceRes.data)
                    }
                }
            }*/
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pay") },
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
        bottomBar = {
            Box(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                SwipeToConfirm(
                    text = "Slide to pay",
                    onConfirmed = {
                        coroutineScope.launch(Dispatchers.IO) {
                            //expenseViewModel.payAllDebts(group.id, phone)
                        }
                        onDone()
                    }
                )
            }

        }
    ) { p ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(p)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo1),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = "You owe",
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurfaceVariant
                )

                Text(
                    text = amount.format(-1, 2),
                    style = MaterialTheme.typography.headlineLarge.copy(color = cs.primary)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    /*items(debts) { debt ->
                        MemberBalanceRow(name = debt.user2.name, balance = debt.amount)
                    }*/
                }
            }
        }
    }
}

@Composable
private fun SwipeToConfirm(
    text: String,
    onConfirmed: () -> Unit,
    heightDp: Int = 56,
    cornerDp: Int = 28
) {
    val cs = MaterialTheme.colorScheme
    val density = LocalDensity.current

    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    val thumbSizeDp = 48.dp
    val thumbSizePx = with(density) { thumbSizeDp.toPx() }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        val maxX = (trackWidthPx - thumbSizePx).coerceAtLeast(0f)
        offsetX = (offsetX + delta).coerceIn(0f, maxX)
    }

    val threshold = 0.9f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightDp.dp)
            .onSizeChanged { size -> trackWidthPx = size.width.toFloat() }
            .clip(RoundedCornerShape(cornerDp.dp))
            .background(cs.surfaceVariant),
        contentAlignment = Alignment.CenterStart
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text, color = cs.onSurfaceVariant, fontSize = 14.sp)
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(thumbSizeDp)
                .clip(CircleShape)
                .background(cs.primary)
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        val maxX = (trackWidthPx - thumbSizePx).coerceAtLeast(0f)
                        if (maxX > 0f && offsetX >= maxX * threshold) {
                            onConfirmed()
                            offsetX = 0f
                        } else {
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Swipe",
                tint = cs.onPrimary
            )
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
