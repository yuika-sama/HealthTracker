package com.yuika.healthtracker.ui.features.auth.forgot_password

import android.util.Patterns
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.domain.usecase.auth_use_cases.CheckEmailExistsUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase
) : BaseViewModel<ForgotPasswordUiState, ForgotPasswordUiIntent, ForgotPasswordUiEffect>(
    initialState = ForgotPasswordUiState()
)
{
    override fun onIntent(intent: ForgotPasswordUiIntent)
    {
        when (intent)
        {
            is ForgotPasswordUiIntent.EmailChanged -> updateState {
                it.copy(
                    email = intent.email,
                    emailError = null,
                    error = null
                )
            }

            is ForgotPasswordUiIntent.SubmitClick -> handleSubmit()
            is ForgotPasswordUiIntent.LoginClick -> handleLogin()
        }
    }

    private fun handleSubmit()
    {
        val email = state.value.email.trim()

        if (email.isEmpty() || email.isBlank())
        {
            updateState { it.copy(emailError = "Email is required") }
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            updateState { it.copy(emailError = "Invalid email format") }
            return
        }

        updateState { it.copy(isLoading = true, error = null, emailError = null) }

        launchSafe(
            onError = { throwable ->
                updateState {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "An unexpected error occurred"
                    )
                }
            }
        ) {
            val exists = checkEmailExistsUseCase(email)
            updateState { it.copy(isLoading = false) }
            if (exists)
            {
                sendEffect(ForgotPasswordUiEffect.NavigateToVerifyOtp(email))
            }
            else
            {
                updateState { it.copy(error = "Account with this email does not exist") }
            }
        }
    }

    private fun handleLogin()
    {
        sendEffect(ForgotPasswordUiEffect.NavigateToLogin)
    }
}