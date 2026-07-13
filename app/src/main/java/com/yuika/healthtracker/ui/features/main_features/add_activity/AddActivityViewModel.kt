package com.yuika.healthtracker.ui.features.main_features.add_activity

import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.domain.repository.ActivityRepository
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class AddActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val activityRepository: ActivityRepository
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
                updateState { it.copy(activityName = intent.name) }
            }
            is AddActivityIntent.OnIconChange -> {
                updateState { it.copy(selectedIcon = intent.iconName) }
            }
            is AddActivityIntent.OnKcalPerHourChange -> {
                updateState { it.copy(kcalPerHour = intent.kcal) }
            }
            is AddActivityIntent.OnDurationChange -> {
                updateState { it.copy(duration = intent.duration) }
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

        val name = currentState.activityName.trim()
        if (name.isEmpty()) {
            sendEffect(AddActivityEffect.ShowError("Vui lòng nhập tên hoạt động"))
            return
        }
        val kcalPerHour = currentState.kcalPerHour.toIntOrNull()
        if (kcalPerHour == null || kcalPerHour <= 0) {
            sendEffect(AddActivityEffect.ShowError("Vui lòng nhập số Kcal/giờ hợp lệ"))
            return
        }
        val duration = currentState.duration.toIntOrNull()
        if (duration == null || duration <= 0) {
            sendEffect(AddActivityEffect.ShowError("Vui lòng nhập thời gian tập luyện hợp lệ"))
            return
        }

        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = {throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(AddActivityEffect.ShowError(throwable.message ?: "Can't save activity"))
            }
        ) {
            val user = userRepository.getLatestUserFlow().firstOrNull()

            if (user != null){
                updateState { it.copy(isLoading = false, errorMessage = "Can't find user information") }
                sendEffect(AddActivityEffect.ShowError("Can't find user information"))
                return@launchSafe
            }

            val activityEntity = ActivityEntity(
                userId = user?.id ?: 0,
                name = name,
                iconName = currentState.selectedIcon,
                kcalPerHour = kcalPerHour,
                durationMins = duration,
                intensity = currentState.selectedIntensity,
                kcalBurned = currentState.estimatedKcalBurned,
                dateText = currentState.dateText
            )

            activityRepository.insertActivity(activityEntity)

            updateState { it.copy(isLoading = false) }
            sendEffect(AddActivityEffect.NavigateToActivity)
        }
    }
}