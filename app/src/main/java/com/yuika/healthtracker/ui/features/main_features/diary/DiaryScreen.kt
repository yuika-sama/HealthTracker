package com.yuika.healthtracker.ui.features.main_features.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuika.healthtracker.R
import com.yuika.healthtracker.ui.features.main_features.diary.components.DailyStats
import com.yuika.healthtracker.ui.features.main_features.diary.components.MealCard
import com.yuika.healthtracker.ui.theme.LocalSpacing
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.DateSelector
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.i18n.foodCatalogLabel
import com.yuika.healthtracker.ui.core.i18n.mealTypeLabel

@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: DiaryViewModel = hiltViewModel(),
    onAddFoodClick: (String, String) -> Unit = {_, _ ->}
) {
    val spacing = LocalSpacing.current

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(DiaryIntent.LoadDiaryData)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.effect.collect { effect ->
                when (effect) {
                    is DiaryEffect.NavigateToAddFood -> onAddFoodClick(effect.mealType, effect.dateText)
                }
            }
        }
    }

    state.selectedDetail?.let { detail ->
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(DiaryIntent.DismissDetail) },
            title = { Text(mealTypeLabel(detail.title)) },
            text = {
                Column {
                    detail.foods.forEach { food ->
                        Text("${foodCatalogLabel(food.name)} - ${food.description} - ${food.kcal} ${stringResource(R.string.unit_kcal)}")
                    }
                    Text(stringResource(R.string.diary_food_total, detail.totalKcal), fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                if (detail.canDelete && detail.foods.isNotEmpty()) {
                    TextButton(onClick = { viewModel.onIntent(DiaryIntent.DeleteFoodClick(detail.foods.first().id)) }) {
                        Text(stringResource(R.string.action_delete))
                    }
                } else {
                    TextButton(onClick = { viewModel.onIntent(DiaryIntent.DismissDetail) }) {
                        Text(stringResource(R.string.action_ok))
                    }
                }
            },
            dismissButton = {
                if (detail.canDelete) {
                    TextButton(onClick = { viewModel.onIntent(DiaryIntent.DismissDetail) }) {
                        Text(stringResource(R.string.action_cancel))
                    }
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing.large),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                DateSelector(
                    selectedDate = state.selectedDate,
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
                    },
                    onDateSelected = { viewModel.onIntent(DiaryIntent.ChangeDate(it)) }
                )
            }

            if (state.errorMessage != null){
                item { ErrorText(msg = state.errorMessage!!) }
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
                        title = stringResource(R.string.meal_breakfast),
                        recommendedKcal = stringResource(R.string.diary_recommended_kcal, (state.totalKcalGoal * 0.25).toInt()),
                        totalKcal = state.breakfastTotalKcal,
                        icon = Icons.Outlined.WbSunny,
                        foods = state.breakfastFoods,
                        onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Breakfast")) },
                        onMealClick = {viewModel.onIntent(DiaryIntent.MealClick("Breakfast"))},
                        onFoodClick = {viewModel.onIntent(DiaryIntent.FoodItemClick(it))}
                    )
                }

                item {
                    MealCard(
                        title = stringResource(R.string.meal_lunch),
                        recommendedKcal = stringResource(R.string.diary_recommended_kcal, (state.totalKcalGoal * 0.35).toInt()),
                        totalKcal = state.lunchTotalKcal,
                        icon = Icons.Outlined.WbSunny,
                        foods = state.lunchFoods,
                        onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Lunch")) },
                        onMealClick = {viewModel.onIntent(DiaryIntent.MealClick("Lunch"))},
                        onFoodClick = {viewModel.onIntent(DiaryIntent.FoodItemClick(it))}
                    )
                }

                item {
                    MealCard(
                        title = stringResource(R.string.meal_dinner),
                        recommendedKcal = stringResource(R.string.diary_recommended_kcal, (state.totalKcalGoal * 0.30).toInt()),
                        totalKcal = state.dinnerTotalKcal,
                        icon = Icons.Outlined.DarkMode,
                        foods = state.dinnerFoods,
                        onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Dinner")) },
                        onMealClick = {viewModel.onIntent(DiaryIntent.MealClick("Dinner"))},
                        onFoodClick = {viewModel.onIntent(DiaryIntent.FoodItemClick(it))}
                    )
                }

                item {
                    MealCard(
                        title = stringResource(R.string.meal_snack),
                        recommendedKcal = stringResource(R.string.diary_snacks_drinks),
                        totalKcal = state.snackTotalKcal,
                        icon = Icons.Outlined.Coffee,
                        foods = state.snackFoods,
                        onAddFoodClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Snack")) },
                        onMealClick = {viewModel.onIntent(DiaryIntent.MealClick("Snack"))},
                        onFoodClick = {viewModel.onIntent(DiaryIntent.FoodItemClick(it))}
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }

        FloatingActionButton(
            onClick = { viewModel.onIntent(DiaryIntent.AddFoodClick("Breakfast")) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(spacing.large),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.action_add))
        }
    }
}
