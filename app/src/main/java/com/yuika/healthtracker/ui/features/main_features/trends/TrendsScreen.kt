package com.yuika.healthtracker.ui.features.main_features.trends

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.R
import com.yuika.healthtracker.service.pdf_exporter.WeeklyReportService
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.StatCard
import com.yuika.healthtracker.ui.features.main_features.trends.components.CalorieIntakeChart
import com.yuika.healthtracker.ui.features.main_features.trends.components.NetCaloriesChart
import com.yuika.healthtracker.ui.theme.LocalSpacing

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
    val shareReportTitle = stringResource(R.string.trends_share_report)
    val dayDetailTitle = stringResource(R.string.trends_day_detail)

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
                            shareReportTitle
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
                    text = stringResource(R.string.trends_title),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.trends_subtitle),
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
                Text(if(state.isExportingReport) stringResource(R.string.trends_exporting) else stringResource(R.string.trends_export_pdf))
            }

            state.selectedDetail?.let { detail ->
                AlertDialog(
                    onDismissRequest = { viewModel.onIntent(TrendsIntent.DismissDetail) },
                    title = { Text(detail.title) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(detail.dateText)
                            Text(stringResource(R.string.trends_intake_kcal, detail.intake))
                            Text(stringResource(R.string.trends_burned_kcal, detail.burned))
                            Text(stringResource(R.string.trends_balance_kcal, detail.balance))
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.onIntent(TrendsIntent.DismissDetail) }) {
                            Text(stringResource(R.string.action_close))
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
                    title = stringResource(R.string.stat_avg_intake_day),
                    value = state.avgIntake,
                    valueColor = MaterialTheme.colorScheme.secondary,
                    unit = "kcal",
                    bgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                )

                StatCard(
                    title = stringResource(R.string.stat_avg_burned_day),
                    value = state.avgBurned,
                    valueColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    unit = "kcal",
                    bgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                )

                StatCard(
                    title = stringResource(R.string.stat_days_meeting_goal),
                    value = state.daysMeetingGoal,
                    valueColor = MaterialTheme.colorScheme.tertiary,
                    unit = stringResource(R.string.trends_goal_days, state.goalDays.toIntOrNull() ?: 0),
                    bgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                )
            }

            if (state.intakeChartData.isNotEmpty())
            {
                CalorieIntakeChart(
                    dataPoints = state.intakeChartData,
                    periodLabel = "${state.startDate} - ${state.endDate}",
                    onPointClick = { viewModel.onIntent(TrendsIntent.PointClick(dayDetailTitle, it)) }
                )
            }

            if (state.weeklyTrendChartData.isNotEmpty())
            {
                NetCaloriesChart(
                    dataPoints = state.weeklyTrendChartData,
                    onPointClick = {
                        viewModel.onIntent(
                            TrendsIntent.PointClick(
                                dayDetailTitle,
                                it
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
    }
}
