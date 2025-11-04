package com.example.msd25_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class GroupSummary(val id: String, val name: String, val balanceDkk: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    groups: List<GroupSummary>,                 // provided by Main with real balances
    onOpenGroup: (groupId: String) -> Unit,
    onCreateGroup: () -> Unit,
    onGoToFriends: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("LOGO", fontWeight = FontWeight.Bold, color = cs.onSurface) }
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
                    GroupCard(group = g, onClick = { onOpenGroup(g.id) })
                }
            }
        }
    }
}

@Composable
private fun GroupCard(group: GroupSummary, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme

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
                val bal = group.balanceDkk
                val text = if (bal >= 0) "+$bal dkk" else "$bal dkk"
                val labelBg = if (bal >= 0) cs.tertiaryContainer else cs.errorContainer
                val labelFg = if (bal >= 0) cs.onTertiaryContainer else cs.onErrorContainer

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
