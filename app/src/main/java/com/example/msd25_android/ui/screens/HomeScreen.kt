package com.example.msd25_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

data class Group(val name: String, val balanceDkk: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenGroup: () -> Unit,
    onCreateGroup: () -> Unit,
    onGoToFriends: () -> Unit // kept for callers; not used here
) {
    val groups = remember {
        listOf(
            Group("Roomies", -500),
            Group("Siblings", 200),
            Group("Trip to Aarhus", -120)
        )
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("LOGO", fontWeight = FontWeight.Bold) }) }
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
                shape = RoundedCornerShape(12.dp)
            ) { Text("CREATE GROUP") }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f, fill = true)
            ) {
                items(groups) { g ->
                    GroupCard(
                        group = g,
                        onClick = onOpenGroup
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupCard(group: Group, onClick: () -> Unit) {
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
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
            ) {
                val bal = group.balanceDkk
                val text = if (bal >= 0) "+${bal} dkk" else "${bal} dkk"
                val labelColor = if (bal >= 0)
                    MaterialTheme.colorScheme.tertiaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
                val onLabel = if (bal >= 0)
                    MaterialTheme.colorScheme.onTertiaryContainer
                else
                    MaterialTheme.colorScheme.onErrorContainer

                Surface(
                    color = labelColor,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = onLabel
                    )
                }
            }
            Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp) {
                Text(
                    text = group.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
