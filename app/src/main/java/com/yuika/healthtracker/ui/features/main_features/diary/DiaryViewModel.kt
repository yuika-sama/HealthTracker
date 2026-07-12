package com.yuika.healthtracker.ui.features.main_features.diary

import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.ui.features.main_features.diary.components.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val foodEntryRepository: FoodEntryRepository
) : BaseViewModel<DiaryUiState, DiaryIntent, DiaryEffect>(
    initialState = DiaryUiState()
) {

    override fun onIntent(intent: DiaryIntent) {
        when(intent){
            is DiaryIntent.LoadDiaryData -> {
                handleFetchDiary(state.value.selectedDate)
            }
            is DiaryIntent.ChangeDate -> {
                handleDateChange(intent.date)
            }
            is DiaryIntent.AddFoodClick -> {
                handleAddFood(intent.mealType)
            }
            is DiaryIntent.FoodItemClick -> {
                // todo: open food item information dialog
            }
        }
    }

    private fun handleAddFood(mealType: String) {
        val validMeals = listOf("Breakfast", "Lunch", "Dinner", "Snack")
        if (mealType !in validMeals){
            sendEffect(DiaryEffect.ShowError("Meal type is not valid"))
            return
        }
        sendEffect(DiaryEffect.NavigateToAddFood(mealType))
    }

    private fun handleDateChange(newDate: LocalDate) {
        val today = LocalDate.now()
        if (newDate.isAfter(today)){
            sendEffect(DiaryEffect.ShowError("Can't write a log a day in future"))
            return
        }

        updateState { it.copy(selectedDate = newDate, errorMessage = null) }
        handleFetchDiary(newDate)
    }

    private fun handleFetchDiary(selectedDate: LocalDate) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(DiaryEffect.ShowError(throwable.message ?: "Can't get diary data"))
            }
        ) {
            val user = userRepository.getLatestUserFlow().firstOrNull()

            if (user == null){
                updateState { it.copy(isLoading = false, errorMessage = "Can't find user information.") }
                sendEffect(DiaryEffect.ShowError("Can't get user data"))
                return@launchSafe
            }

            val dateText = selectedDate.toString()

            val allFoods = foodEntryRepository.getFoodEntriesByDate(user.id, dateText).firstOrNull() ?: emptyList()

            val breakfastEntries = allFoods.filter { it.mealType.equals("Breakfast", ignoreCase = true) }
            val lunchEntries = allFoods.filter { it.mealType.equals("Lunch", ignoreCase = true) }
            val dinnerEntries = allFoods.filter { it.mealType.equals("Dinner", ignoreCase = true) }
            val snackEntries = allFoods.filter { it.mealType.equals("Snack", ignoreCase = true) }

            val breakfastKcal = breakfastEntries.sumOf { it.calories }
            val lunchKcal = lunchEntries.sumOf { it.calories }
            val dinnerKcal = dinnerEntries.sumOf { it.calories }
            val snackKcal = snackEntries.sumOf { it.calories }

            val totalConsumed = breakfastKcal + lunchKcal + dinnerKcal + snackKcal

            fun List<FoodEntryEntity>.toUiModels() = this.map {
                FoodItem(
                    name = it.foodName,
                    description = "${it.quantity} ${it.unit}",
                    kcal = it.calories,
                )
            }

            val weight = user.weight
            val height = user.height
            val age = user.age

            var bmr = (10 * weight) + (6.25 * height) - (5 * age)
            bmr += if (user.gender == "Male") 5.0 else -161.0

            val activityMultiplier = when (user.activityLevel) {
                "sedentary" -> 1.2
                "lightly_active" -> 1.375
                "moderately_active" -> 1.55
                "very_active" -> 1.725
                "extra_active" -> 1.9
                else -> 1.55
            }

            var tdee = bmr * activityMultiplier

            when (user.goal) {
                "lose_weight" -> tdee -= 500
                "gain_weight" -> tdee += 500
            }

            updateState { currentState ->
                currentState.copy(
                    isLoading = false,
                    totalKcalGoal = tdee.toInt(),
                    totalKcalConsumed = totalConsumed,
                    proteinGrams = 0,
                    fatGrams = 0,
                    carbsGrams = 0,

                    breakfastFoods = breakfastEntries.toUiModels(),
                    lunchFoods = lunchEntries.toUiModels(),
                    dinnerFoods = dinnerEntries.toUiModels(),
                    snackFoods = snackEntries.toUiModels(),

                    breakfastTotalKcal = breakfastKcal,
                    lunchTotalKcal = lunchKcal,
                    dinnerTotalKcal = dinnerKcal,
                    snackTotalKcal = snackKcal
                )
            }
        }
    }
}