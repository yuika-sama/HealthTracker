package com.yuika.healthtracker.ui.features.auth.create_new_password

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndResetPasswordUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.ui.navigation.Route
import com.yuika.healthtracker.utils.NETWORK_DELAY
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class CreateNewPasswordViewModel @Inject constructor(
    private val validateAndResetPasswordUseCase: ValidateAndResetPasswordUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CreateNewPasswordUiState, CreateNewPasswordIntent, CreateNewPasswordEffect>(
    initialState = CreateNewPasswordUiState()
) {
    private val route = savedStateHandle.toRoute<Route.CreateNewPassword>()

    override fun onIntent(intent: CreateNewPasswordIntent) {
        when (intent) {
            is CreateNewPasswordIntent.ShowNewPassword -> updateState {
                it.copy(isShowNewPassword = !it.isShowNewPassword)
            }

            is CreateNewPasswordIntent.NewPasswordChanged -> {
                val strength = calculateStrength(intent.password)
                updateState {
                    it.copy(
                        newPassword = intent.password,
                        newPasswordStrength = strength,
                        newPasswordError = null,
                        errorMessage = null,
                        isSuccess = false
                    )
                }
            }

            is CreateNewPasswordIntent.ConfirmNewPasswordChanged -> updateState {
                it.copy(
                    confirmNewPassword = intent.confirmPassword,
                    confirmNewPasswordError = null,
                    errorMessage = null,
                    isSuccess = false
                )
            }

            is CreateNewPasswordIntent.ShowConfirmNewPassword -> updateState {
                it.copy(isShowConfirmNewPassword = !it.isShowConfirmNewPassword)
            }

            is CreateNewPasswordIntent.ResetPasswordClick -> handleResetPassword()
        }
    }

    private fun handleResetPassword() {
        val currentState = state.value
        if (!validatePasswordInput(currentState)) return

        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                newPasswordError = null,
                confirmNewPasswordError = null,
                isSuccess = false
            )
        }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "An unexpected error occurred"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
                sendEffect(CreateNewPasswordEffect.ShowToast(message))
            }
        ) {
            delay(NETWORK_DELAY.toLong())
            validateAndResetPasswordUseCase(
                email = route.email,
                newPassword = currentState.newPassword,
                confirmPassword = currentState.confirmNewPassword
            )
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(CreateNewPasswordEffect.NavigateToPasswordChanged)
        }
    }

    private fun validatePasswordInput(currentState: CreateNewPasswordUiState): Boolean {
        val newPassword = currentState.newPassword
        val confirmPassword = currentState.confirmNewPassword

        val newPasswordError = when {
            newPassword.isBlank() -> "Password cannot be empty"
            newPassword.length < 8 -> "Password must be at least 8 characters"
            !PASSWORD_REGEX.matches(newPassword) -> "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
            else -> null
        }

        val confirmPasswordError = when {
            confirmPassword.isBlank() -> "Password cannot be empty"
            confirmPassword != newPassword -> "Password do not match"
            else -> null
        }

        val hasError = newPasswordError != null || confirmPasswordError != null
        if (hasError) {
            updateState {
                it.copy(
                    isLoading = false,
                    newPasswordError = newPasswordError,
                    confirmNewPasswordError = confirmPasswordError,
                    errorMessage = null,
                    isSuccess = false
                )
            }
        }

        return !hasError
    }

    private fun calculateStrength(password: String): Int {
        return when {
            password.isEmpty() -> 0
            password.length < 6 -> 1
            password.length < 8 -> 2
            password.length < 10 -> 3
            else -> 4
        }
    }
}
