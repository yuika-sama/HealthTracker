package com.yuika.healthtracker.ui.features.main_features.diary

import com.yuika.healthtracker.domain.usecase.main_use_cases.diary.DiaryFoodItem
import com.yuika.healthtracker.domain.usecase.main_use_cases.diary.GetDiaryDataUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.diary.ValidateDiaryLogicUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.ui.features.main_features.diary.components.FoodItem
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getDiaryDataUseCase: GetDiaryDataUseCase,
    private val validateDiaryLogicUseCase: ValidateDiaryLogicUseCase
) : BaseViewModel<DiaryUiState, DiaryIntent, DiaryEffect>(
    initialState = DiaryUiState()
) {

    override fun onIntent(intent: DiaryIntent) {
        when(intent){
            is DiaryIntent.LoadDiaryData -> handleFetchDiary(state.value.selectedDate)
            is DiaryIntent.ChangeDate -> handleDateChange(intent.date)
            is DiaryIntent.AddFoodClick -> handleAddFood(intent.mealType)
            is DiaryIntent.FoodItemClick -> Unit
        }
    }

    private fun handleAddFood(mealType: String) {
        try {
            validateDiaryLogicUseCase.validateMealType(mealType)
            updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
            launchSafe(
                onError = {throwable ->
                    val message = throwable.message ?: "Unknown error occurred."
                    updateState { it.copy(isLoading = false, errorMessage = message, isSuccess = false) }
                    sendEffect(DiaryEffect.ShowError(message))
                }
            ) {
                delay(NETWORK_DELAY.toLong())
                updateState { it.copy(isLoading = false, isSuccess = true, errorMessage = null) }
                sendEffect(DiaryEffect.NavigateToAddFood(mealType))
            }
        } catch (e: Exception) {
            sendEffect(DiaryEffect.ShowError(e.message ?: "Invalid meal type"))
        }
    }

    private fun handleDateChange(newDate: LocalDate) {
        try {
            validateDiaryLogicUseCase.validateDate(newDate)
            updateState { it.copy(selectedDate = newDate, errorMessage = null) }
            handleFetchDiary(newDate)
        } catch (e: Exception) {
            sendEffect(DiaryEffect.ShowError(e.message ?: "Invalid date"))
        }
    }

    private var fetchJob: Job? = null

    private fun handleFetchDiary(selectedDate: LocalDate) {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Can't get diary data"
                updateState { it.copy(isLoading = false, errorMessage = message, isSuccess = false) }
                sendEffect(DiaryEffect.ShowError(message))
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
                    FoodItem(name = it.name, description = it.description, kcal = it.kcal)
                }

                delay(NETWORK_DELAY.toLong())

                updateState { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isSuccess = true,
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
