package com.yuika.healthtracker.ui.features.main_features.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.R
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SuccessText
import com.yuika.healthtracker.ui.core.i18n.bmiCategoryLabel
import com.yuika.healthtracker.ui.core.i18n.currentLocale
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DailySummaryCard
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.InfoBanner
import com.yuika.healthtracker.ui.core.components.StatCard
import com.yuika.healthtracker.ui.theme.EnergyAmber
import com.yuika.healthtracker.ui.theme.InfoBlue
import com.yuika.healthtracker.ui.theme.LocalSpacing
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: DashboardViewModel = hiltViewModel(),
    onAddMealClick: () -> Unit = {},
    onAddActivityClick: () -> Unit = {}
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val locale = currentLocale()
    val dateFormatter = remember(locale) { DateTimeFormatter.ofPattern("MMM, dd", locale) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val adviceText = when {
        state.goalCalories <= 0 -> stringResource(R.string.dashboard_update_profile_target)
        state.remainingCalories < 0 -> stringResource(R.string.dashboard_over_target_today, -state.remainingCalories)
        state.remainingCalories <= 300 -> stringResource(R.string.dashboard_close_to_target)
        else -> stringResource(R.string.dashboard_remaining_today, state.remainingCalories)
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(DashboardIntent.LoadDashboardData)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect)
                {
                    is DashboardEffect.NavigateToDiary -> onAddMealClick()
                    is DashboardEffect.NavigateToActivity -> onAddActivityClick()
                }
            }
        }
    }

    if (state.isBreakdownVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(DashboardIntent.DismissBreakdown) },
            title = { Text(stringResource(R.string.dashboard_breakdown_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.dashboard_target_kcal, state.goalCalories))
                    Text("${stringResource(R.string.stat_tdee)}: ${state.tdeeCalories} ${stringResource(R.string.unit_kcal)}")
                    Text(stringResource(R.string.dashboard_eaten_kcal, state.intakeCalories))
                    Text(stringResource(R.string.calories_burned_kcal, state.burnedCalories))
                    Text(stringResource(R.string.calories_balance_kcal, state.netBalance))
                    Text(
                        if (state.remainingCalories < 0)
                            stringResource(R.string.dashboard_over_kcal, -state.remainingCalories)
                        else
                            stringResource(R.string.dashboard_remaining_kcal, state.remainingCalories)
                    )
                    Text("${stringResource(R.string.stat_bmi)}: ${state.bmi} ${bmiCategoryLabel(state.bmiCategory)}")
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onIntent(DashboardIntent.DismissBreakdown) }) {
                    Text(stringResource(R.string.action_close))
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = spacing.large)
            .verticalScroll(scrollState)
    ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.dashboard_today_date, LocalDate.now().format(dateFormatter)),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            state.errorMessageRes?.let { ErrorText(stringResource(it)) }

//            if (state.isSuccess && !state.isLoading && state.errorMessageRes == null)
//            {
//                SuccessText("Dashboard loaded")
//            }

            if (state.isLoading)
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
            else
            {
                InfoBanner(message = adviceText)

                Spacer(modifier = Modifier.height(24.dp))

                DailySummaryCard(
                    targetKcal = state.goalCalories,
                    eatenKcal = state.intakeCalories,
                    burnedKcal = state.burnedCalories,
                    remainingKcal = state.remainingCalories,
                    balanceKcal = state.netBalance,
                    progress = state.progressFraction,
                    onClick = {viewModel.onIntent(DashboardIntent.SummaryClick)}
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.stat_tdee),
                            value = "${state.tdeeCalories}",
                            icon = Icons.Outlined.LocalFireDepartment,
                            iconTint = EnergyAmber,
                            iconBgColor = EnergyAmber.copy(alpha = 0.15f)
                        )

                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.stat_bmi),
                            value = "${state.bmi}",
                            unit = state.bmiCategory.takeIf { it.isNotBlank() }?.let { bmiCategoryLabel(it) },
                            icon = Icons.Outlined.Balance,
                            iconTint = InfoBlue,
                            iconBgColor = InfoBlue.copy(alpha = 0.15f)
                        )
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.stat_intake),
                            value = "${state.intakeCalories}",
                            icon = Icons.Outlined.LocalDining,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )

                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.stat_burned),
                            value = "${state.burnedCalories}",
                            icon = Icons.Outlined.LocalFireDepartment,
                            iconTint = EnergyAmber,
                            iconBgColor = EnergyAmber.copy(alpha = 0.15f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                StatCard(
                    title = stringResource(R.string.dashboard_net_balance),
                    value = "${state.netBalance}",
                    icon = Icons.Outlined.Balance,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    iconBgColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Add Buttons
                Button(
                    onClick = {
                        viewModel.onIntent(DashboardIntent.AddMealClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.dashboard_more_meal),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.onIntent(DashboardIntent.AddActivityClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.dashboard_more_activity),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
    }
}
