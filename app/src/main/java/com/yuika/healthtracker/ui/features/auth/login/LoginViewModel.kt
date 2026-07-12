package com.yuika.healthtracker.ui.features.auth.login

import android.util.Patterns
import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.domain.usecase.auth_use_cases.LoginUseCase
import com.yuika.healthtracker.domain.usecase.auth_use_cases.OAuthLoginUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.MOCK_ERROR_LOGIN_EMAIL
import com.yuika.healthtracker.utils.MOCK_OAUTH_ACCOUNT_ID
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val oAuthLoginUseCase: OAuthLoginUseCase
) : BaseViewModel<LoginUiState, LoginUiIntent, LoginUiEffect>(
    initialState = LoginUiState()
)
{
    override fun onIntent(intent: LoginUiIntent)
    {
        when (intent)
        {
            is LoginUiIntent.EmailChanged -> updateState {
                it.copy(
                    email = intent.email,
                    errorMessage = null,
                    emailErrorMessage = null
                )
            }

            is LoginUiIntent.PasswordChanged -> updateState {
                it.copy(
                    password = intent.password,
                    errorMessage = null,
                    passwordErrorMessage = null
                )
            }

            is LoginUiIntent.LoginClick -> handleLogin()
            is LoginUiIntent.ShowPasswordClick -> updateState { it.copy(isShowPassword = !it.isShowPassword) }
            is LoginUiIntent.RememberAccountClick -> updateState { it.copy(isRememberAccount = !it.isRememberAccount) }
            is LoginUiIntent.ForgotPasswordClick -> handleForgotPassword()
            is LoginUiIntent.RegisterClick -> handleRegister()
            is LoginUiIntent.GoogleClick -> handleOAuthLogin("Google")
            is LoginUiIntent.FacebookClick -> handleOAuthLogin("Facebook")
        }
    }

    private fun handleLogin()
    {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        val email = state.value.email.trim()
        val password = state.value.password.trim()

        var hasError = false
        var emailErr: String? = null
        var passwordErr: String? = null

        // Validate Input
        if (email.isEmpty())
        {
            emailErr = "Email would not be blank"
            hasError = true
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailErr = "Email format is invalid"
            hasError = true
        }

        if (password.isEmpty())
        {
            passwordErr = "Password would not be blank"
            hasError = true
        }
        else if (password.length < state.value.passwordLength)
        {
            passwordErr = "Password length would be longer than ${state.value.passwordLength}"
            hasError = true
        }

        if (hasError)
        {
            updateState {
                it.copy(
                    emailErrorMessage = emailErr,
                    passwordErrorMessage = passwordErr
                )
            }
            return
        }

        updateState { it.copy(isLoading = true, errorMessage = null) }

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
            loginUseCase(email, password)
            // TODO: handle save session token into datastore
            // if(state.value.isRememberAccount) -> save into datastore
            updateState { it.copy(isLoading = false) }
            sendEffect(LoginUiEffect.NavigateToDashboard)
        }
    }

    private fun handleOAuthLogin(provider: String)
    {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = { throwable ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "OAuth Login error - $provider: ${throwable.message}"
                    )
                }
            }
        ) {
            oAuthLoginUseCase(provider)
            updateState { it.copy(isLoading = false) }
            sendEffect(LoginUiEffect.ShowToast("$provider Login Success"))
            sendEffect(LoginUiEffect.NavigateToDashboard)
        }
    }

    private fun handleRegister()
    {
        sendEffect(LoginUiEffect.NavigateToRegister)
    }

    private fun handleForgotPassword()
    {
        sendEffect(LoginUiEffect.NavigateToForgotPassword)
    }
}