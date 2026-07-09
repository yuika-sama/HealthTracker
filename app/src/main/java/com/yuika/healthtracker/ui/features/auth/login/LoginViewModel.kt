package com.yuika.healthtracker.ui.features.auth.login

import android.util.Log
import androidx.compose.animation.core.animateIntOffsetAsState
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor
(
    // todo: repository
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
                    errorMessage = null
                )
            }

            is LoginUiIntent.PasswordChanged -> updateState {
                it.copy(
                    password = intent.password,
                    errorMessage = null
                )
            }

            is LoginUiIntent.LoginClick -> handleLogin()
            is LoginUiIntent.ShowPasswordClick -> updateState { it.copy(isShowPassword = !it.isShowPassword) }
            is LoginUiIntent.RememberAccountClick -> updateState { it.copy(isRememberAccount = !it.isRememberAccount) }
            is LoginUiIntent.ForgotPasswordClick -> handleForgotPassword()
            is LoginUiIntent.RegisterClick -> handleRegister()
            is LoginUiIntent.GoogleClick -> handleGoogleClick()
            is LoginUiIntent.FacebookClick -> handleFacebookClick()
        }
    }

    private fun handleFacebookClick()
    {
        updateState { it.copy(isLoading = true, errorMessage = null) }

    }

    private fun handleGoogleClick()
    {
        TODO("Not yet implemented")
    }

    private fun handleRegister()
    {
        TODO("Not yet implemented")
    }

    private fun handleForgotPassword()
    {
        TODO("Not yet implemented")
    }

    private fun handleLogin()
    {
        TODO("Not yet implemented")
    }
}