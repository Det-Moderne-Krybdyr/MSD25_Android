package com.example.msd25_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msd25_android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayScreen(
    amount: Double,
    onDone: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    var slider by remember { mutableStateOf(0f) }
    val threshold = 0.95f

    LaunchedEffect(slider) {
        if (slider >= threshold) {
            onDone()
            slider = 0f
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("PAY") }) }
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
                    text = "%.2f kr".format(amount),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = cs.primary
                    )
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Slide to pay", color = cs.onSurfaceVariant, fontSize = 14.sp)
                    Slider(
                        value = slider,
                        onValueChange = { slider = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = cs.primary,
                            activeTrackColor = cs.primary,
                            inactiveTrackColor = cs.surfaceVariant
                        )
                    )
                }

                Button(
                    onClick = onDone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.primary,
                        contentColor = cs.onPrimary
                    )
                ) {
                    Text("PAY NOW", fontSize = 18.sp)
                }
            }
        }
    }
}
