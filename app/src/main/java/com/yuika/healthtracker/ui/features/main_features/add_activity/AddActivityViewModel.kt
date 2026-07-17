package com.yuika.healthtracker.ui.features.main_features.add_activity

import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.GetActivityCatalogUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.ValidateAndSaveActivityUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.service.widget.WidgetService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class AddActivityViewModel @Inject constructor(
    private val validateAndSaveActivityUseCase: ValidateAndSaveActivityUseCase,
    private val getActivityCatalogUseCase: GetActivityCatalogUseCase,
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val widgetService: WidgetService
) : BaseViewModel<AddActivityUiState, AddActivityIntent, AddActivityEffect>(
    initialState = AddActivityUiState()
)
{
    override fun onIntent(intent: AddActivityIntent) {
        when (intent) {
            is AddActivityIntent.Init -> loadData(intent.dateText)
            is AddActivityIntent.OnActivitySelected -> updateState {
                it.copy(selectedActivity = intent.activity, activityCatalogError = null)
            }
            is AddActivityIntent.OnDurationChange -> {
                updateState { it.copy(duration = intent.duration, durationError = null) }
            }
            is AddActivityIntent.OnSaveClick -> {
                handleSaveActivity()
            }
        }
    }

    private fun loadData(dateText: String)
    {
        updateState { it.copy(dateText = dateText, errorMessage = null) }

        launchSafe(
            onError = { throwable ->
                updateState { it.copy(errorMessage = throwable.message ?: "Can't load activity catalog") }
            }
        ) {
            val user = getLatestUserUseCase().firstOrNull()
            val catalog = getActivityCatalogUseCase().firstOrNull().orEmpty()
            updateState {
                it.copy(
                    userWeightKg = user?.weight ?: 0.0,
                    activityCatalogs = catalog,
                    selectedActivity = it.selectedActivity ?: catalog.firstOrNull()
                )
            }
        }
    }

    private fun handleSaveActivity()
    {
        val currentState = state.value

        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                activityCatalogError = null,
                durationError = null,
                isSuccess = false
            )
        }

        launchSafe(
            onError = {throwable ->
                val message = throwable.message ?: "Unknown error"
                updateState { it.copy(isLoading = false, errorMessage = message, isSuccess = false) }
            }
        ) {
            validateAndSaveActivityUseCase(
                selectedActivity = currentState.selectedActivity,
                durationStr = currentState.duration,
                dateText = currentState.dateText
            )

            widgetService.refresh()

            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(AddActivityEffect.NavigateToActivity)
        }
    }
}
