package com.yuika.healthtracker.ui.features.auth.register

import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndRegisterUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateAndRegisterUseCase: ValidateAndRegisterUseCase
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

            is RegisterIntent.AgeChanged -> updateState {
                it.copy(
                    age = intent.age,
                    ageError = null,
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

        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                emailError = null,
                fullNameError = null,
                ageError = null,
                passwordError = null,
                confirmPasswordError = null
            )
        }

        launchSafe(
            onError = { throwable ->
                val  msg = throwable.message ?: "Unknown error occurred"
                when {
                    msg.startsWith("FullName_") -> updateState { it.copy(isLoading = false, fullNameError = msg.removePrefix("FullName_")) }
                    msg.startsWith("Email_") -> updateState { it.copy(isLoading = false, emailError = msg.removePrefix("Email_")) }
                    msg.startsWith("Age_") -> updateState { it.copy(isLoading = false, ageError = msg.removePrefix("Age_")) }
                    msg.startsWith("Password_") -> updateState { it.copy(isLoading = false, passwordError = msg.removePrefix("Password_")) }
                    msg.startsWith("ConfirmPassword_") -> updateState { it.copy(isLoading = false, confirmPasswordError = msg.removePrefix("ConfirmPassword_")) }
                    else -> updateState { it.copy(isLoading = false, errorMessage = msg) }
                }
            }
        ) {
            validateAndRegisterUseCase(
                fullName = currentState.fullName,
                email = currentState.email,
                age =  currentState.age,
                password = currentState.password,
                confirmPassword = currentState.confirmPassword
            )
            delay(NETWORK_DELAY.toLong())
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(RegisterEffect.NavigateToVerifyOtp(currentState.email))
        }
    }
}
