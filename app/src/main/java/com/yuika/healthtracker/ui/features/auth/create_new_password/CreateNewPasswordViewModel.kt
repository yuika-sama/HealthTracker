package com.yuika.healthtracker.ui.features.auth.create_new_password

import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ResetPasswordUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndCheckEmailUseCase
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndResetPasswordUseCase
import com.yuika.healthtracker.ui.navigation.Route

@HiltViewModel
class CreateNewPasswordViewModel @Inject constructor(
    private val validateAndResetPasswordUseCase: ValidateAndResetPasswordUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CreateNewPasswordUiState, CreateNewPasswordIntent, CreateNewPasswordEffect>(
    initialState = CreateNewPasswordUiState()
)
{
    private val route = savedStateHandle.toRoute<Route.CreateNewPassword>()
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

        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = { throwable ->
                val msg = throwable.message ?: "An unexpected error occurred"
                when {
                    msg.startsWith("NewPassword_") -> updateState { it.copy(isLoading = false, newPasswordError = msg.removePrefix("NewPassword_")) }
                    msg.startsWith("ConfirmPassword_") -> updateState { it.copy(isLoading = false, confirmNewPasswordError = msg.removePrefix("ConfirmPassword_")) }
                    else -> {
                        updateState { it.copy(isLoading = false, errorMessage = msg) }
                        sendEffect(CreateNewPasswordEffect.ShowToast(msg))
                    }
                }
            }
        ) {
            validateAndResetPasswordUseCase(route.email, newPassword, confirmPassword)

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