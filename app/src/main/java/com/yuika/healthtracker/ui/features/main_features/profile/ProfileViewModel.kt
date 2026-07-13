package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.GetProfileDataUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase
) : BaseViewModel<ProfileUiState, ProfileIntent, ProfileEffect>(
    initialState = ProfileUiState()
) {

    override fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfile -> handleLoadProfile()
            is ProfileIntent.Logout -> handleLogout()
        }
    }

    private var fetchJob: Job? = null

    private fun handleLoadProfile() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        
        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(ProfileEffect.ShowError(throwable.message ?: "Error loading information"))
            }
        ) {
            getProfileDataUseCase().collectLatest { profileData ->
                if (profileData == null) {
                    updateState { it.copy(isLoading = false, errorMessage = "Can't find user information") }
                    sendEffect(ProfileEffect.ShowError("Can't find user information"))
                    return@collectLatest
                }
                
                updateState {
                    it.copy(
                        name = profileData.name,
                        subtitle = profileData.subtitle,
                        weight = profileData.weight,
                        height = profileData.height,
                        bmi = profileData.bmi,
                        goalTitle = profileData.goalTitle,
                        goalDescription = profileData.goalDescription,
                        isLoading = false
                    )
                }
            }
        }
    }
    
    private fun handleLogout() {
        // Todo: clear session/token, delete DataStore, then navigate
        sendEffect(ProfileEffect.NavigateToLogin)
    }
}
