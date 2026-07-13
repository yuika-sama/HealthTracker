package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<ProfileUiState, ProfileIntent, ProfileEffect>(
    initialState = ProfileUiState()
) {

    override fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfile -> handleLoadProfile()
            is ProfileIntent.Logout -> handleLogout()
        }
    }

    private fun handleLoadProfile() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        
        launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(ProfileEffect.ShowError(throwable.message ?: "Error loading information"))
            }
        ) {
            val user = userRepository.getLatestUserFlow().firstOrNull()
            
            if (user == null) {
                updateState { it.copy(isLoading = false, errorMessage = "Can't find user information") }
                sendEffect(ProfileEffect.ShowError("Can't find user information"))
                return@launchSafe
            }
            
            // Tính BMI
            val hMeter = user.height / 100.0
            val bmi = if (hMeter > 0) user.weight / (hMeter * hMeter) else 0.0
            
            // Tính TDEE
            var bmr = (10 * user.weight) + (6.25 * user.height) - (5 * user.age)
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
            val goalTitle = when (user.goal) {
                "lose_weight" -> { tdee -= 500; "Lose weight" }
                "gain_weight" -> { tdee += 500; "Gain weight" }
                else -> "Maintain weight"
            }
            val goalKcal = tdee.toInt()
            
            val activityStr = when (user.activityLevel) {
                "sedentary" -> "Sedentary"
                "lightly_active" -> "Lightly active"
                "moderately_active" -> "Moderately active"
                "very_active" -> "Very active"
                "extra_active" -> "Extra active"
                else -> "Active"
            }
            
            val goalDesc = "$goalTitle. Mục tiêu nạp ${String.format("%,d", goalKcal).replace(',', '.')} kcal mỗi ngày."
            
            updateState {
                it.copy(
                    name = user.name,
                    subtitle = activityStr,
                    weight = "${user.weight} kg",
                    height = "${user.height} cm",
                    bmi = String.format("%.1f", bmi).replace(',', '.'),
                    goalTitle = "Current goal",
                    goalDescription = goalDesc,
                    isLoading = false
                )
            }
        }
    }
    
    private fun handleLogout() {
        // Todo: clear session/token, delete DataStore, then navigate
        sendEffect(ProfileEffect.NavigateToLogin)
    }
}
