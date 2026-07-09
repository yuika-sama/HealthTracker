package com.yuika.healthtracker.ui.features.auth.register

import android.util.Patterns
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.domain.usecase.auth_use_cases.RegisterUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.ui.features.auth.login.LoginUiEffect
import com.yuika.healthtracker.ui.features.auth.login.LoginUiIntent
import com.yuika.healthtracker.ui.features.auth.login.LoginUiState
import com.yuika.healthtracker.utils.MOCK_ERROR_LOGIN_EMAIL
import com.yuika.healthtracker.utils.NETWORK_DELAY
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel<RegisterUiState, RegisterIntent, RegisterEffect>(
    initialState = RegisterUiState()
)
{
    override fun onIntent(intent: RegisterIntent)
    {
        when (intent)
        {
            is RegisterIntent.FullNameChanged -> updateState {
                it.copy(
                    fullName = intent.fullName,
                    fullNameError = null,
                    errorMessage = null
                )
            }

            is RegisterIntent.EmailChanged -> updateState {
                it.copy(
                    email = intent.email,
                    emailError = null,
                    errorMessage = null
                )
            }

            is RegisterIntent.PasswordChanged -> updateState {
                it.copy(
                    password = intent.password,
                    passwordError = null,
                    errorMessage = null
                )
            }

            is RegisterIntent.ConfirmPasswordChanged -> updateState {
                it.copy(
                    confirmPassword = intent.confirmPassword,
                    confirmPasswordError = null,
                    errorMessage = null
                )
            }

            is RegisterIntent.ShowPasswordChanged -> updateState { it.copy(showPassword = !it.showPassword) }
            is RegisterIntent.ShowConfirmPasswordChanged -> updateState {
                it.copy(
                    showConfirmPassword = !it.showConfirmPassword
                )
            }

            is RegisterIntent.AgreedToTermsChanged -> updateState { it.copy(agreedToTerms = !it.agreedToTerms) }
            is RegisterIntent.Submit -> handleSubmit()
        }
    }

    private fun handleSubmit()
    {
        val currentState = state.value

        var hasError = false
        var fullNameErr: String? = null
        var emailErr: String? = null
        var passwordErr: String? = null
        var confirmPasswordErr: String? = null

        if (currentState.fullName.isEmpty() || currentState.fullName.isBlank())
        {
            fullNameErr = "Name would not be blank"
            hasError = true
        }

        if (currentState.email.isBlank() || currentState.email.isEmpty())
        {
            emailErr = "Email would not be blank"
            hasError = true
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches())
        {
            emailErr = "Email format is invalid"
            hasError = true
        }

        if (currentState.password.isBlank() || currentState.password.isEmpty())
        {
            passwordErr = "Password would not be blank"
            hasError = true
        }
        else if (currentState.password.length < currentState.passwordLength)
        {
            passwordErr = "Password length would be longer than ${currentState.passwordLength}"
            hasError = true
        }
        else if (!PASSWORD_REGEX.matches(currentState.password))
        {
            passwordErr =
                "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
            hasError = true
        }

        if (currentState.confirmPassword.isBlank())
        {
            confirmPasswordErr = "Confirm password would not be blank"
            hasError = true
        }
        else if (currentState.confirmPassword != currentState.password)
        {
            confirmPasswordErr = "Confirm password do not match"
            hasError = true
        }

        if (hasError)
        {
            updateState {
                it.copy(
                    fullNameError = fullNameErr,
                    emailError = emailErr,
                    passwordError = passwordErr,
                    confirmPasswordError = confirmPasswordErr
                )
            }
            return
        }

        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        launchSafe(
            onError = { throwable ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unknown error occurred"
                    )
                }
            }
        ) {
            registerUseCase(currentState.email)
            updateState { it.copy(isLoading = false) }
            sendEffect(RegisterEffect.NavigateToVerifyOtp(currentState.email))
        }
    }
}
