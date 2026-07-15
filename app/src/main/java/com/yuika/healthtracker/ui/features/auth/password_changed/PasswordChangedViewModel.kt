package com.yuika.healthtracker.ui.features.auth.password_changed

import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class PasswordChangedViewModel @Inject constructor(
) : BaseViewModel<PasswordChangedUiState, PasswordChangedIntent, PasswordChangedEffect>(
    initialState = PasswordChangedUiState()
)
{
    override fun onIntent(intent: PasswordChangedIntent)
    {
        when (intent)
        {
            is PasswordChangedIntent.BackToLoginClick -> navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Can't navigate to login"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
            }
        ) {
            delay(NETWORK_DELAY.toLong())
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(PasswordChangedEffect.NavigateToLogin)
        }
    }
}
