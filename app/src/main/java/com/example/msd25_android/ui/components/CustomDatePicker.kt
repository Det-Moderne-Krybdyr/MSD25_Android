import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import kotlinx.datetime.format.char

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(modifier: Modifier = Modifier, state: DatePickerState) {
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }

    val cs = MaterialTheme.colorScheme

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = cs.primary,
        unfocusedBorderColor = cs.primary.copy(alpha = 0.5f),
        focusedLabelColor = cs.primary,
        unfocusedLabelColor = cs.onSurfaceVariant,
        disabledLabelColor = cs.onSurface,
        disabledTextColor = cs.onSurface
    )

    if (isDatePickerOpen) {
        DatePickerDialog(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(onClick = { isDatePickerOpen = false }) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDatePickerOpen = false }) {
                    Text("Cancel")
                }
            },
            content = {
                DatePicker(
                    state = state,
                    showModeToggle = true,
                    title = {
                        Text(
                            text = "Select your birthday",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                )
            }
        )
    }


    Column(
        //modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = state.selectedDateMillis.toFormattedDateString(),
            onValueChange = { },
            enabled = false,
            label = { Text("Birthdate") },
            singleLine = true,
            trailingIcon = {
                Button(onClick = { isDatePickerOpen = true }) {
                    Text("Choose")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors
        )
    }
}

fun Long?.toFormattedDateString(): String {
    val instant = Instant.fromEpochMilliseconds(this ?: System.currentTimeMillis())
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    val formatter = LocalDate.Format {
        // Format: 25/02/2025
        dayOfMonth(); char('/'); monthNumber(); char('/'); year()
    }
    return formatter.format(date)
}