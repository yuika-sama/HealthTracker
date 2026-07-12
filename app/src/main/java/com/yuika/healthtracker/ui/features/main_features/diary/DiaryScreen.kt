package com.yuika.healthtracker.ui.features.main_features.diary

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar
import com.yuika.healthtracker.ui.features.main_features.diary.components.DailyStats
import com.yuika.healthtracker.ui.features.main_features.diary.components.DateSelector
import com.yuika.healthtracker.ui.features.main_features.diary.components.MealCard
import com.yuika.healthtracker.ui.theme.LocalSpacing
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalLocale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle

@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier,
    viewModel: DiaryViewModel = hiltViewModel(),
    onAddFoodClick: (String) -> Unit = {},
    onTabClick: (String) -> Unit = {}
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.effect.collect { effect ->
                when (effect) {
                    is DiaryEffect.NavigateToAddFood -> onAddFoodClick(effect.mealType)
                    is DiaryEffect.ShowError -> {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT)
                    }
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.large)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            
            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", LocalLocale.current.platformLocale)
            
            DateSelector(
                dateText = state.selectedDate.format(dateFormatter),
                onPreviousDayClick = { viewModel.onIntent(DiaryIntent.ChangeDate(state.selectedDate.minusDays(1))) },
                onNextDayClick = { viewModel.onIntent(DiaryIntent.ChangeDate(state.selectedDate.plusDays(1))) }
            )
            
            DailyStats(
                eatenKcal = state.totalKcalConsumed,
                remainingKcal = maxOf(0, state.totalKcalGoal - state.totalKcalConsumed),
                burnedKcal = 0
            )
            
            MealCard(
                title = "Breakfast",
                recommendedKcal = "Recommended: ${(state.totalKcalGoal * 0.25).toInt()} kcal",
                totalKcal = state.breakfastTotalKcal,
                icon = Icons.Outlined.WbSunny,
                foods = state.breakfastFoods,
                onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Breakfast")) }
            )
            
            MealCard(
                title = "Lunch",
                recommendedKcal = "Recommended: ${(state.totalKcalGoal * 0.35).toInt()} kcal",
                totalKcal = state.lunchTotalKcal,
                icon = Icons.Outlined.WbSunny,
                foods = state.lunchFoods,
                onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Lunch")) }
            )
            
            MealCard(
                title = "Dinner",
                recommendedKcal = "Recommended: ${(state.totalKcalGoal * 0.30).toInt()} kcal",
                totalKcal = state.dinnerTotalKcal,
                icon = Icons.Outlined.DarkMode,
                foods = state.dinnerFoods,
                onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Dinner")) }
            )
            
            MealCard(
                title = "Snack",
                recommendedKcal = "Snacks & Drinks",
                totalKcal = state.snackTotalKcal,
                icon = Icons.Outlined.Coffee,
                foods = state.snackFoods,
                onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Snack")) }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiaryScreenPreview() {
    DiaryScreen()
}
