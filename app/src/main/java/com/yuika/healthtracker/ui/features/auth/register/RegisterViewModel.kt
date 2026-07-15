package com.yuika.healthtracker.ui.features.auth.register

import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndRegisterUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
        if (!currentState.agreedToTerms) {
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = "Please agree to the terms before continuing",
                    isSuccess = false
                )
            }
            return
        }

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
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(RegisterEffect.NavigateToVerifyOtp(currentState.email.trim()))
        }
    }
}
