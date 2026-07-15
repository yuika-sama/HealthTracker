package com.yuika.healthtracker.ui.features.main_features.trends

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SegmentedSelector
import com.yuika.healthtracker.ui.core.components.StatCard
import com.yuika.healthtracker.ui.core.components.SuccessText
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar
import com.yuika.healthtracker.ui.features.main_features.trends.components.CalorieIntakeChart
import com.yuika.healthtracker.ui.features.main_features.trends.components.NetCaloriesChart
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun TrendsScreen(
    modifier: Modifier = Modifier,
    viewModel: TrendsViewModel = hiltViewModel(),
    onTabClick: (String) -> Unit = {}
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.onIntent(TrendsIntent.LoadTrendsData)
    }

    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        bottomBar = {
            DashboardBottomNav(currentRoute = "trends", onTabClick = onTabClick)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
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

            SegmentedSelector(
                options = listOf("Week", "Month"),
                selectedOption = state.selectedPeriod,
                onOptionSelected = { viewModel.onIntent(TrendsIntent.OnPeriodChange(it)) }
            )

            if (state.errorMessage != null)
            {
                ErrorText(msg = state.errorMessage!!)
            }

            if (state.isSuccess && !state.isLoading && state.errorMessage == null)
            {
                SuccessText(msg = "Trends loaded")
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
                    periodLabel = if (state.selectedPeriod == "Week") "This Week" else "This Month"
                )
            }

            if (state.netCaloriesChartData.isNotEmpty())
            {
                NetCaloriesChart(
                    dataPoints = state.netCaloriesChartData
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
