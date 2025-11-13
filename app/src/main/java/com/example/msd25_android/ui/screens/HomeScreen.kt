package com.example.msd25_android.ui.screens

import android.app.Application
import android.icu.math.BigDecimal
import android.icu.math.MathContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msd25_android.R
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.viewmodels.ExpenseViewModel
import com.example.msd25_android.logic.viewmodels.UserViewModel
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class GroupSummary(val id: String, val name: String, val balanceDkk: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenGroup: (group: Group) -> Unit,
    onCreateGroup: () -> Unit,
    userViewModel: UserViewModel = viewModel(),
) {
    val cs = MaterialTheme.colorScheme
    val groups = remember { mutableStateListOf<Group>() }
    var phone by remember { mutableStateOf("") }

    val application = LocalContext.current.applicationContext as Application
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            phone = UserRepository(application.dataStore).currentPhoneNumber.first()!!
            val res = userViewModel.getUserWithGroups(phone)
            if (res.success) groups.addAll(res.data!!.groups)
        }
    }

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
                .padding(16.dp)
        ) {
            Button(
                onClick = onCreateGroup,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.primary,
                    contentColor = cs.onPrimary
                )
            ) { Text("Create Group") }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f, fill = true)
            ) {
                items(groups, key = { it.id }) { g ->
                    GroupCard(group = g, onClick = { onOpenGroup(g) }, phone = phone)
                }
            }
        }
    }
}

@Composable
private fun GroupCard(
    group: Group,
    phone: String,
    onClick: () -> Unit,
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    val cs = MaterialTheme.colorScheme
    var balanceDkk by remember { mutableStateOf(BigDecimal.ZERO) }

    val coroutineScope = rememberCoroutineScope();

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val res = expenseViewModel.getAmountOwed(group.id, phone)
            if (res.success) balanceDkk = res.data!!
        }
    }


    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(cs.primaryContainer, cs.secondaryContainer)
                        )
                    )
            ) {
                val text = if (balanceDkk > BigDecimal.ZERO) "+${balanceDkk.format(-1, 2)} dkk" else "${balanceDkk.format(-1, 2)} dkk"
                val labelBg = if (balanceDkk >= BigDecimal.ZERO) cs.tertiaryContainer else cs.errorContainer
                val labelFg = if (balanceDkk >= BigDecimal.ZERO) cs.onTertiaryContainer else cs.onErrorContainer

                Surface(
                    color = labelBg,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = labelFg
                    )
                }
            }
            Surface(color = cs.surface, tonalElevation = 1.dp) {
                Text(
                    text = group.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )
            }
        }
    }
}
