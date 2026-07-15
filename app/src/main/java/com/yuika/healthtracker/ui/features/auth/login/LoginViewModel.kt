package com.yuika.healthtracker.ui.features.auth.login

import android.util.Patterns
import com.yuika.healthtracker.domain.usecase.auth_use_cases.OAuthLoginUseCase
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndLoginUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateAndLoginUseCase: ValidateAndLoginUseCase,
    private val oAuthLoginUseCase: OAuthLoginUseCase
) : BaseViewModel<LoginUiState, LoginUiIntent, LoginUiEffect>(
    initialState = LoginUiState()
) {
    override fun onIntent(intent: LoginUiIntent) {
        when (intent) {
            is LoginUiIntent.EmailChanged -> updateState {
                it.copy(
                    email = intent.email,
                    errorMessage = null,
                    emailErrorMessage = null,
                    isSuccess = false
                )
            }

            is LoginUiIntent.PasswordChanged -> updateState {
                it.copy(
                    password = intent.password,
                    errorMessage = null,
                    passwordErrorMessage = null,
                    isSuccess = false
                )
            }

            is LoginUiIntent.LoginClick -> handleLogin()
            is LoginUiIntent.ShowPasswordClick -> updateState { it.copy(isShowPassword = !it.isShowPassword) }
            is LoginUiIntent.RememberAccountClick -> updateState { it.copy(isRememberAccount = !it.isRememberAccount) }
            is LoginUiIntent.ForgotPasswordClick -> navigateWithLoading(LoginUiEffect.NavigateToForgotPassword)
            is LoginUiIntent.RegisterClick -> navigateWithLoading(LoginUiEffect.NavigateToRegister)
            is LoginUiIntent.GoogleClick -> handleOAuthLogin("Google")
            is LoginUiIntent.FacebookClick -> handleOAuthLogin("Facebook")
        }
    }

    private fun handleLogin() {
        val currentState = state.value
        if (!validateLoginInput(currentState)) return

        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                emailErrorMessage = null,
                passwordErrorMessage = null,
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
                sendEffect(LoginUiEffect.ShowToast(message))
            }
        ) {
            delay(NETWORK_DELAY.toLong())
            validateAndLoginUseCase(currentState.email, currentState.password)
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(LoginUiEffect.NavigateToDashboard)
        }
    }

    private fun validateLoginInput(currentState: LoginUiState): Boolean {
        val email = currentState.email.trim()
        val password = currentState.password

        val emailError = when {
            email.isBlank() -> "Email would not be blank"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email format is invalid"
            else -> null
        }

        val passwordError = when {
            password.isBlank() -> "Password would not be blank"
            password.length < 8 -> "Password length must be at least 8 characters"
            !PASSWORD_REGEX.matches(password) -> "Password format is invalid"
            else -> null
        }

        val hasError = emailError != null || passwordError != null
        if (hasError) {
            updateState {
                it.copy(
                    isLoading = false,
                    emailErrorMessage = emailError,
                    passwordErrorMessage = passwordError,
                    errorMessage = null,
                    isSuccess = false
                )
            }
        }
        return !hasError
    }

    private fun handleOAuthLogin(provider: String) {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        launchSafe(
            onError = { throwable ->
                val message = "OAuth Login error - $provider: ${throwable.message ?: "Unknown error"}"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
                sendEffect(LoginUiEffect.ShowToast(message))
            }
        ) {
            oAuthLoginUseCase(provider)
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(LoginUiEffect.ShowToast("$provider Login Success"))
            sendEffect(LoginUiEffect.NavigateToDashboard)
        }
    }

    private fun navigateWithLoading(effect: LoginUiEffect) {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                emailErrorMessage = null,
                passwordErrorMessage = null,
                isSuccess = false
            )
        }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Can't continue"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
                sendEffect(LoginUiEffect.ShowToast(message))
            }
        ) {
            delay(NETWORK_DELAY.toLong())
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(effect)
        }
    }
}
