package com.yuika.healthtracker.ui.features.main_features.diary

import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.domain.usecase.main_use_cases.diary.DiaryFoodItem
import com.yuika.healthtracker.domain.usecase.main_use_cases.diary.GetDiaryDataUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.ui.features.main_features.diary.components.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getDiaryDataUseCase: GetDiaryDataUseCase
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

    private var fetchJob: Job? = null

    private fun handleFetchDiary(selectedDate: LocalDate) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(DiaryEffect.ShowError(throwable.message ?: "Can't get diary data"))
            }
        ) {
            val dateText = selectedDate.toString()

            getDiaryDataUseCase(dateText).collectLatest { diaryData ->
                if (diaryData == null) {
                    updateState { it.copy(isLoading = false, errorMessage = "Can't find user information.") }
                    sendEffect(DiaryEffect.ShowError("Can't get user data"))
                    return@collectLatest
                }

                fun List<DiaryFoodItem>.toUiModels() = this.map {
                    FoodItem(
                        name = it.name,
                        description = it.description,
                        kcal = it.kcal
                    )
                }

                updateState { currentState ->
                    currentState.copy(
                        isLoading = false,
                        totalKcalGoal = diaryData.totalKcalGoal,
                        totalKcalConsumed = diaryData.totalKcalConsumed,
                        proteinGrams = diaryData.proteinGrams,
                        fatGrams = diaryData.fatGrams,
                        carbsGrams = diaryData.carbsGrams,

                        breakfastFoods = diaryData.breakfastFoods.toUiModels(),
                        lunchFoods = diaryData.lunchFoods.toUiModels(),
                        dinnerFoods = diaryData.dinnerFoods.toUiModels(),
                        snackFoods = diaryData.snackFoods.toUiModels(),

                        breakfastTotalKcal = diaryData.breakfastTotalKcal,
                        lunchTotalKcal = diaryData.lunchTotalKcal,
                        dinnerTotalKcal = diaryData.dinnerTotalKcal,
                        snackTotalKcal = diaryData.snackTotalKcal,
                        errorMessage = null
                    )
                }
            }
        }
    }
}