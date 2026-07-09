package com.yuika.healthtracker.ui.features.auth.create_new_password

import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ResetPasswordUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNewPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseViewModel<CreateNewPasswordUiState, CreateNewPasswordIntent, CreateNewPasswordEffect>(
    initialState = CreateNewPasswordUiState()
)
{
    override fun onIntent(intent: CreateNewPasswordIntent)
    {
        when (intent)
        {
            is CreateNewPasswordIntent.ShowNewPassword -> updateState { it.copy(isShowNewPassword = !it.isShowNewPassword) }
            is CreateNewPasswordIntent.NewPasswordChanged ->
            {
                val strength = calculateStrength(intent.password)
                updateState {
                    it.copy(
                        newPassword = intent.password,
                        newPasswordStrength = strength,
                        newPasswordError = null,
                        errorMessage = null
                    )
                }
            }

            is CreateNewPasswordIntent.NewPasswordStrengthChanged -> updateState {
                it.copy(
                    newPasswordStrength = intent.strength
                )
            }

            is CreateNewPasswordIntent.ConfirmNewPasswordChanged -> updateState {
                it.copy(
                    confirmNewPassword = intent.confirmPassword,
                    confirmNewPasswordError = null,
                    errorMessage = null
                )
            }

            is CreateNewPasswordIntent.ShowConfirmNewPassword -> updateState {
                it.copy(
                    isShowConfirmNewPassword = !it.isShowConfirmNewPassword
                )
            }

            is CreateNewPasswordIntent.ResetPasswordClick -> handleResetPassword()
        }
    }

    private fun handleResetPassword()
    {
        val newPassword = state.value.newPassword
        val confirmPassword = state.value.confirmNewPassword

        if (newPassword.isBlank() || newPassword.isEmpty())
        {
            updateState { it.copy(newPasswordError = "Password cannot be empty") }
            return
        }
        if (newPassword.length < 8)
        {
            updateState { it.copy(newPasswordError = "Password must be at least 8 characters") }
            return
        }
        if (!PASSWORD_REGEX.matches(newPassword))
        {
            updateState { it.copy(newPasswordError = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character") }
            return
        }

        if (confirmPassword.isBlank() || confirmPassword.isEmpty())
        {
            updateState { it.copy(confirmNewPasswordError = "Password cannot be empty") }
            return
        }

        if (confirmPassword != newPassword)
        {
            updateState { it.copy(confirmNewPasswordError = "Password do not match") }
            return
        }

        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = { throwable ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "An unexpected error occurred"
                    )
                }
                sendEffect(
                    CreateNewPasswordEffect.ShowToast(
                        throwable.message ?: "An unexpected error occurred"
                    )
                )
            }
        ) {
            // Todo: find user by email
            resetPasswordUseCase(null, newPassword)

            updateState { it.copy(isLoading = false) }
            sendEffect(CreateNewPasswordEffect.NavigateToPasswordChanged)
        }
    }

    private fun calculateStrength(password: String): Int
    {
        return when
        {
            password.isEmpty() -> 0
            password.length < 6 -> 1
            password.length < 8 -> 2
            password.length < 10 -> 3
            else -> 4
        }
    }
}