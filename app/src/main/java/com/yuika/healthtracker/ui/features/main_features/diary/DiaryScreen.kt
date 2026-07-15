package com.yuika.healthtracker.ui.features.main_features.diary

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar
import com.yuika.healthtracker.ui.features.main_features.diary.components.DailyStats
import com.yuika.healthtracker.ui.features.main_features.diary.components.DateSelector
import com.yuika.healthtracker.ui.features.main_features.diary.components.MealCard
import com.yuika.healthtracker.ui.theme.LocalSpacing
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SuccessText
import com.yuika.healthtracker.utils.DATE_FORMATTER

@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier,
    viewModel: DiaryViewModel = hiltViewModel(),
    onAddFoodClick: (String) -> Unit = {},
    onTabClick: (String) -> Unit = {}
) {
    val spacing = LocalSpacing.current

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(DiaryIntent.LoadDiaryData)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.effect.collect { effect ->
                when (effect) {
                    is DiaryEffect.NavigateToAddFood -> onAddFoodClick(effect.mealType)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        bottomBar = {
            DashboardBottomNav(currentRoute = "diary", onTabClick = onTabClick)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Breakfast")) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.large),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                DateSelector(
                    dateText = state.selectedDate.format(DATE_FORMATTER),
                    onPreviousDayClick = {
                        viewModel.onIntent(
                            DiaryIntent.ChangeDate(
                                state.selectedDate.minusDays(
                                    1
                                )
                            )
                        )
                    },
                    onNextDayClick = {
                        viewModel.onIntent(
                            DiaryIntent.ChangeDate(
                                state.selectedDate.plusDays(
                                    1
                                )
                            )
                        )
                    }
                )
            }

            if (state.errorMessage != null){
                item { ErrorText(msg = state.errorMessage!!) }
            }

            if (state.isSuccess && !state.isLoading && state.errorMessage == null) {
                item { SuccessText(msg = "Diary loaded") }
            }
            
            if (state.isLoading){
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        LoadingIndicator()
                    }
                }
            } else {
                item {
                    DailyStats(
                        eatenKcal = state.totalKcalConsumed,
                        remainingKcal = maxOf(0, state.totalKcalGoal - state.totalKcalConsumed),
                        burnedKcal = 0
                    )
                }

                item {
                    MealCard(
                        title = "Breakfast",
                        recommendedKcal = "Recommended: ${(state.totalKcalGoal * 0.25).toInt()} kcal",
                        totalKcal = state.breakfastTotalKcal,
                        icon = Icons.Outlined.WbSunny,
                        foods = state.breakfastFoods,
                        onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Breakfast")) }
                    )
                }

                item {
                    MealCard(
                        title = "Lunch",
                        recommendedKcal = "Recommended: ${(state.totalKcalGoal * 0.35).toInt()} kcal",
                        totalKcal = state.lunchTotalKcal,
                        icon = Icons.Outlined.WbSunny,
                        foods = state.lunchFoods,
                        onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Lunch")) }
                    )
                }

                item {
                    MealCard(
                        title = "Dinner",
                        recommendedKcal = "Recommended: ${(state.totalKcalGoal * 0.30).toInt()} kcal",
                        totalKcal = state.dinnerTotalKcal,
                        icon = Icons.Outlined.DarkMode,
                        foods = state.dinnerFoods,
                        onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Dinner")) }
                    )
                }

                item {
                    MealCard(
                        title = "Snack",
                        recommendedKcal = "Snacks & Drinks",
                        totalKcal = state.snackTotalKcal,
                        icon = Icons.Outlined.Coffee,
                        foods = state.snackFoods,
                        onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Snack")) }
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}
