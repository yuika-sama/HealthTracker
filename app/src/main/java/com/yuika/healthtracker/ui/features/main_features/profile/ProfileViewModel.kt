package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.GetProfileDataUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase
) : BaseViewModel<ProfileUiState, ProfileIntent, ProfileEffect>(
    initialState = ProfileUiState()
)
{

    override fun onIntent(intent: ProfileIntent)
    {
        when (intent)
        {
            is ProfileIntent.LoadProfile -> handleLoadProfile()
            is ProfileIntent.EditProfile -> navigateWithLoading(ProfileEffect.NavigateToEditProfile)
            is ProfileIntent.Logout -> handleLogout()
        }
    }

    private var fetchJob: Job? = null

    private fun handleLoadProfile()
    {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Error loading information"
                updateState { it.copy(isLoading = false, errorMessage = message) }
                sendEffect(ProfileEffect.ShowError(message))
            }
        ) {
            getProfileDataUseCase().collectLatest { profileData ->
                if (profileData == null)
                {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Can't find user information",
                            isSuccess = false
                        )
                    }
                    sendEffect(ProfileEffect.ShowError("Can't find user information"))
                    return@collectLatest
                }

                delay(NETWORK_DELAY.toLong())

                updateState {
                    it.copy(
                        name = profileData.name,
                        subtitle = profileData.subtitle,
                        weight = profileData.weight,
                        height = profileData.height,
                        bmi = profileData.bmi,
                        goalTitle = profileData.goalTitle,
                        goalDescription = profileData.goalDescription,
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun handleLogout()
    {
        navigateWithLoading(ProfileEffect.NavigateToLogin)
    }

    private fun navigateWithLoading(effect: ProfileEffect)
    {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Can't continue"
                updateState { it.copy(isLoading = false, errorMessage = message) }
                sendEffect(ProfileEffect.ShowError(message))
            }
        ) {
            delay(NETWORK_DELAY.toLong())
            // todo: clear session/token, delete datastore then navigate if logout
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(effect)
        }
    }
}
