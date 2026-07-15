package com.yuika.healthtracker.ui.features.main_features.add_activity

import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.ValidateAndSaveActivityUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddActivityViewModel @Inject constructor(
    private val validateAndSaveActivityUseCase: ValidateAndSaveActivityUseCase
) : BaseViewModel<AddActivityUiState, AddActivityIntent, AddActivityEffect>(
    initialState = AddActivityUiState()
)
{
    override fun onIntent(intent: AddActivityIntent) {
        when (intent) {
            is AddActivityIntent.Init -> {
                updateState { it.copy(dateText = intent.dateText) }
            }
            is AddActivityIntent.OnActivityNameChange -> {
                updateState { it.copy(activityName = intent.name, activityNameError = null) }
            }
            is AddActivityIntent.OnIconChange -> {
                updateState { it.copy(selectedIcon = intent.iconName) }
            }
            is AddActivityIntent.OnKcalPerHourChange -> {
                updateState { it.copy(kcalPerHour = intent.kcal, kcalPerHourError = null) }
            }
            is AddActivityIntent.OnDurationChange -> {
                updateState { it.copy(duration = intent.duration, durationError = null) }
            }
            is AddActivityIntent.OnIntensityChange -> {
                updateState { it.copy(selectedIntensity = intent.intensity) }
            }
            is AddActivityIntent.OnSaveClick -> {
                handleSaveActivity()
            }
        }
    }

    private fun handleSaveActivity()
    {
        val currentState = state.value

        updateState { it.copy(isLoading = true, errorMessage = null, activityNameError = null, kcalPerHourError = null, durationError = null, isSuccess = false) }

        launchSafe(
            onError = {throwable ->
                val message = throwable.message ?: "Unknown error"
                updateState { it.copy(isLoading = false, errorMessage = message, isSuccess = false) }
            }
        ) {
            validateAndSaveActivityUseCase(
                activityName = currentState.activityName,
                selectedIcon = currentState.selectedIcon,
                kcalPerHourStr = currentState.kcalPerHour,
                durationStr = currentState.duration,
                selectedIntensity = currentState.selectedIntensity,
                estimatedKcalBurned = currentState.estimatedKcalBurned,
                dateText = currentState.dateText
            )

            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(AddActivityEffect.NavigateToActivity)
        }
    }
}
