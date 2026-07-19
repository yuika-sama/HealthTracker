package com.yuika.healthtracker.ui.features.main_features.trends

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.service.pdf_exporter.WeeklyReportService
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.StatCard
import com.yuika.healthtracker.ui.features.main_features.trends.components.CalorieIntakeChart
import com.yuika.healthtracker.ui.features.main_features.trends.components.NetCaloriesChart
import com.yuika.healthtracker.ui.theme.LocalSpacing
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: TrendsViewModel = hiltViewModel()
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        viewModel.onIntent(TrendsIntent.LoadTrendsData)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is TrendsEffect.ShareWeeklyReport -> {
                        val chooser = Intent.createChooser(
                            WeeklyReportService.shareIntent(effect.uri),
                            "Share weekly report"
                        )
                        chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        context.startActivity(chooser)
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = spacing.large)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            Spacer(modifier = Modifier.height(4.dp))

            Column {
                Text(
                    text = "Statistics & Trends",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Track your progress and analyze your habits.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            DateRangeSelector(
                startDate = state.startDate,
                endDate = state.endDate,
                onRangeSelected = { startDate, endDate ->
                    viewModel.onIntent(TrendsIntent.ChangeDateRange(startDate, endDate))
                }
            )

            Button(
                onClick = {viewModel.onIntent(TrendsIntent.ExportWeeklyReportClick)},
                enabled = !state.isExportingReport,
                modifier = Modifier.fillMaxWidth()
            ){
                Icon(imageVector = Icons.Outlined.FileDownload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if(state.isExportingReport) "Exporting..." else "Export selected range PDF")
            }

            state.selectedDetail?.let { detail ->
                AlertDialog(
                    onDismissRequest = { viewModel.onIntent(TrendsIntent.DismissDetail) },
                    title = { Text(detail.title) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(detail.dateText)
                            Text("Intake: ${detail.intake} kcal")
                            Text("Burned: ${detail.burned} kcal")
                            Text("Balance: ${detail.balance} kcal")
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.onIntent(TrendsIntent.DismissDetail) }) {
                            Text("Close")
                        }
                    }
                )
            }

            if (state.errorMessage != null)
            {
                ErrorText(msg = state.errorMessage!!)
            }

            if (state.isLoading)
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
            else
            {
                StatCard(
                    title = "Avg Intake / Day",
                    value = state.avgIntake,
                    valueColor = MaterialTheme.colorScheme.secondary,
                    unit = "kcal",
                    bgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                )

                StatCard(
                    title = "Avg Burned / Day",
                    value = state.avgBurned,
                    valueColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    unit = "kcal",
                    bgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                )

                StatCard(
                    title = "Days Meeting Goal",
                    value = state.daysMeetingGoal,
                    valueColor = MaterialTheme.colorScheme.tertiary,
                    unit = state.goalDays,
                    bgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                )
            }

            if (state.intakeChartData.isNotEmpty())
            {
                CalorieIntakeChart(
                    dataPoints = state.intakeChartData,
                    periodLabel = state.rangeLabel(),
                    onPointClick = { viewModel.onIntent(TrendsIntent.PointClick("Day detail", it)) }
                )
            }

            if (state.weeklyTrendChartData.isNotEmpty())
            {
                NetCaloriesChart(
                    dataPoints = state.weeklyTrendChartData,
                    onPointClick = {
                        viewModel.onIntent(
                            TrendsIntent.PointClick(
                                "Day detail",
                                it
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangeSelector(
    startDate: LocalDate,
    endDate: LocalDate,
    onRangeSelected: (LocalDate, LocalDate) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    if (showPicker) {
        val rangeState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = startDate.toEpochMillis(),
            initialSelectedEndDateMillis = endDate.toEpochMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    enabled = rangeState.selectedStartDateMillis != null &&
                            rangeState.selectedEndDateMillis != null,
                    onClick = {
                        val startMillis = rangeState.selectedStartDateMillis
                        val endMillis = rangeState.selectedEndDateMillis
                        if (startMillis != null && endMillis != null) {
                            onRangeSelected(startMillis.toLocalDate(), endMillis.toLocalDate())
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
                    val selectedStart = rangeState.selectedStartDateMillis?.toLocalDate()
                    val selectedEnd = rangeState.selectedEndDateMillis?.toLocalDate()
                    Text(
                        text = when {
                            selectedStart != null && selectedEnd != null ->
                                "${selectedStart.compactDate()} - ${selectedEnd.compactDate()}"
                            selectedStart != null -> "${selectedStart.compactDate()} - ..."
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

private fun TrendsUiState.rangeLabel() = "$startDate - $endDate"

private fun LocalDate.compactDate() = format(DateRangeLabelFormatter)

private val DateRangeLabelFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)

private fun LocalDate.toEpochMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
