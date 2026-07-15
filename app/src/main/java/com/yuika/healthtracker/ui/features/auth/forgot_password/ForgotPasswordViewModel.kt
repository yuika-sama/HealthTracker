package com.yuika.healthtracker.ui.features.auth.forgot_password

import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndCheckEmailUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val validateAndCheckEmailUseCase: ValidateAndCheckEmailUseCase
) : BaseViewModel<ForgotPasswordUiState, ForgotPasswordUiIntent, ForgotPasswordUiEffect>(
    initialState = ForgotPasswordUiState()
) {
    override fun onIntent(intent: ForgotPasswordUiIntent) {
        when (intent) {
            is ForgotPasswordUiIntent.EmailChanged -> updateState {
                it.copy(
                    email = intent.email,
                    emailError = null,
                    error = null,
                    isSuccess = false
                )
            }

            is ForgotPasswordUiIntent.SubmitClick -> handleSubmit()
            is ForgotPasswordUiIntent.LoginClick -> handleLogin()
        }
    }

    private fun handleSubmit() {
        val email = state.value.email.trim()

        updateState {
            it.copy(
                isLoading = true,
                error = null,
                emailError = null,
                isSuccess = false
            )
        }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "An unexpected error occurred"
                updateState {
                    it.copy(
                        isLoading = false,
                        error = message,
                        isSuccess = false
                    )
                }
                sendEffect(ForgotPasswordUiEffect.ShowToast(message))
            }
        ) {
            val exists = validateAndCheckEmailUseCase(email)
            if (exists) {
                updateState { it.copy(isLoading = false, isSuccess = true) }
                sendEffect(ForgotPasswordUiEffect.NavigateToVerifyOtp(email))
            } else {
                val message = "Account with this email does not exist"
                updateState {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = message
                    )
                }
                sendEffect(ForgotPasswordUiEffect.ShowToast(message))
            }
        }
    }

    private fun handleLogin() {
        updateState { it.copy(isLoading = true, error = null, isSuccess = false) }
        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "An unexpected error occurred"
                updateState { it.copy(isLoading = false, error = message, isSuccess = false) }
                sendEffect(ForgotPasswordUiEffect.ShowToast(message))
            }
        ) {
            delay(NETWORK_DELAY.toLong())
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(ForgotPasswordUiEffect.NavigateToLogin)
        }
    }
}
