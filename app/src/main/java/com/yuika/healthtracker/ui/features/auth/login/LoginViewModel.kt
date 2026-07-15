package com.yuika.healthtracker.ui.features.auth.login

import com.yuika.healthtracker.domain.usecase.auth_use_cases.OAuthLoginUseCase
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndLoginUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
            is LoginUiIntent.ForgotPasswordClick -> sendEffect(LoginUiEffect.NavigateToForgotPassword)
            is LoginUiIntent.RegisterClick -> sendEffect(LoginUiEffect.NavigateToRegister)
            is LoginUiIntent.GoogleClick -> handleOAuthLogin("Google")
            is LoginUiIntent.FacebookClick -> handleOAuthLogin("Facebook")
        }
    }

    private fun handleLogin() {
        val currentState = state.value

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
            }
        ) {
            validateAndLoginUseCase(currentState.email, currentState.password)
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(LoginUiEffect.NavigateToDashboard)
        }
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
            }
        ) {
            oAuthLoginUseCase(provider)
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(LoginUiEffect.ShowToast("$provider Login Success"))
            sendEffect(LoginUiEffect.NavigateToDashboard)
        }
    }

}
