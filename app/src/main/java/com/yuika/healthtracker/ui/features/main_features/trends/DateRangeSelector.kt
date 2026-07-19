package com.yuika.healthtracker.ui.features.main_features.trends

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSelector(
    startDate: LocalDate,
    endDate: LocalDate,
    onRangeSelected: (LocalDate, LocalDate) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val labelFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH) }
    val startMillis = remember(startDate) {
        startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
    val endMillis = remember(endDate) {
        endDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    if (showPicker) {
        val rangeState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = startMillis,
            initialSelectedEndDateMillis = endMillis
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    enabled = rangeState.selectedStartDateMillis != null &&
                            rangeState.selectedEndDateMillis != null,
                    onClick = {
                        val selectedStart = rangeState.selectedStartDateMillis
                        val selectedEnd = rangeState.selectedEndDateMillis
                        if (selectedStart != null && selectedEnd != null) {
                            val start = Instant.ofEpochMilli(selectedStart)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            val end = Instant.ofEpochMilli(selectedEnd)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            onRangeSelected(start, end)
                        }
                        showPicker = false
                    }
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(
                state = rangeState,
                title = {
                    Text(
                        text = "Date range",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 18.dp)
                    )
                },
                headline = {
                    val selectedStart = rangeState.selectedStartDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    val selectedEnd = rangeState.selectedEndDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    Text(
                        text = when {
                            selectedStart != null && selectedEnd != null ->
                                "${selectedStart.format(labelFormatter)} - ${selectedEnd.format(labelFormatter)}"
                            selectedStart != null -> "${selectedStart.format(labelFormatter)} - ..."
                            else -> "Select start and end dates"
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                },
                showModeToggle = false
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { showPicker = true }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Date range",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$startDate - $endDate",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
