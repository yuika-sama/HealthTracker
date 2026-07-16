package com.yuika.healthtracker.ui.features.main_features.diary

import com.yuika.healthtracker.ui.core.base.UiState
import com.yuika.healthtracker.ui.features.main_features.diary.components.FoodItem
import java.time.LocalDate

data class DiaryUiState(
    val isLoading: Boolean = false,

    val selectedDate: LocalDate = LocalDate.now(),

    val totalKcalGoal: Int = 0,
    val totalKcalConsumed: Int = 0,
    val proteinGrams: Int = 0,
    val fatGrams: Int = 0,
    val carbsGrams: Int = 0,

    val breakfastFoods: List<FoodItem> = emptyList(),
    val lunchFoods: List<FoodItem> = emptyList(),
    val dinnerFoods: List<FoodItem> = emptyList(),
    val snackFoods: List<FoodItem> = emptyList(),

    val breakfastTotalKcal: Int = 0,
    val lunchTotalKcal: Int = 0,
    val dinnerTotalKcal: Int = 0,
    val snackTotalKcal: Int = 0,

    val selectedDetail: DiaryDetail? = null,

    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState


data class DiaryDetail(
    val title: String,
    val foods: List<FoodItem>,
    val totalKcal: Int,
    val canDelete: Boolean,
)
