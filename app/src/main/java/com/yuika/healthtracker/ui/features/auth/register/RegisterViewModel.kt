package com.yuika.healthtracker.ui.features.auth.register

import android.util.Patterns
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndRegisterUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateAndRegisterUseCase: ValidateAndRegisterUseCase
) : BaseViewModel<RegisterUiState, RegisterIntent, RegisterEffect>(
    initialState = RegisterUiState()
) {
    override fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.FullNameChanged -> updateState {
                it.copy(fullName = intent.fullName, fullNameError = null, errorMessage = null, isSuccess = false)
            }

            is RegisterIntent.EmailChanged -> updateState {
                it.copy(email = intent.email, emailError = null, errorMessage = null, isSuccess = false)
            }

            is RegisterIntent.AgeChanged -> updateState {
                it.copy(age = intent.age, ageError = null, errorMessage = null, isSuccess = false)
            }

            is RegisterIntent.PasswordChanged -> updateState {
                it.copy(password = intent.password, passwordError = null, errorMessage = null, isSuccess = false)
            }

            is RegisterIntent.ConfirmPasswordChanged -> updateState {
                it.copy(
                    confirmPassword = intent.confirmPassword,
                    confirmPasswordError = null,
                    errorMessage = null,
                    isSuccess = false
                )
            }

            is RegisterIntent.ShowPasswordChanged -> updateState { it.copy(showPassword = !it.showPassword) }
            is RegisterIntent.ShowConfirmPasswordChanged -> updateState {
                it.copy(showConfirmPassword = !it.showConfirmPassword)
            }

            is RegisterIntent.AgreedToTermsChanged -> updateState { it.copy(agreedToTerms = !it.agreedToTerms) }
            is RegisterIntent.Submit -> handleSubmit()
        }
    }

    private fun handleSubmit() {
        val currentState = state.value
        if (!validateRegisterInput(currentState)) return

        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                emailError = null,
                fullNameError = null,
                ageError = null,
                passwordError = null,
                confirmPasswordError = null,
                isSuccess = false
            )
        }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Unknown error occurred"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
            }
        ) {
            validateAndRegisterUseCase(
                fullName = currentState.fullName,
                email = currentState.email,
                age = currentState.age,
                password = currentState.password,
                confirmPassword = currentState.confirmPassword
            )
            delay(NETWORK_DELAY.toLong())
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(RegisterEffect.NavigateToVerifyOtp(currentState.email.trim()))
        }
    }

    private fun validateRegisterInput(currentState: RegisterUiState): Boolean {
        val fullName = currentState.fullName.trim()
        val email = currentState.email.trim()
        val age = currentState.age.toIntOrNull()
        val password = currentState.password
        val confirmPassword = currentState.confirmPassword

        val fullNameError = if (fullName.isBlank()) "Name would not be blank" else null
        val emailError = when {
            email.isBlank() -> "Email would not be blank"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email format is invalid"
            else -> null
        }
        val ageError = when {
            currentState.age.isBlank() -> "Age would not be blank"
            age == null || age !in 10..120 -> "Please enter a valid age (10-120)"
            else -> null
        }
        val passwordError = when {
            password.isBlank() -> "Password would not be blank"
            password.length < 8 -> "Password length must be at least 8 characters"
            !PASSWORD_REGEX.matches(password) -> "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
            else -> null
        }
        val confirmPasswordError = when {
            confirmPassword.isBlank() -> "Confirm password would not be blank"
            confirmPassword != password -> "Confirm password do not match"
            else -> null
        }
        val termsError = if (!currentState.agreedToTerms) "Please agree to the terms before continuing" else null

        val hasError = listOf(
            fullNameError,
            emailError,
            ageError,
            passwordError,
            confirmPasswordError,
            termsError
        ).any { it != null }

        if (hasError) {
            updateState {
                it.copy(
                    isLoading = false,
                    fullNameError = fullNameError,
                    emailError = emailError,
                    ageError = ageError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError,
                    errorMessage = termsError,
                    isSuccess = false
                )
            }
        }

        return !hasError
    }
}
