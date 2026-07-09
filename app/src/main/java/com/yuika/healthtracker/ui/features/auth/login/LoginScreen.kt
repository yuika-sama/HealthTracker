package com.yuika.healthtracker.ui.features.auth.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuika.healthtracker.ui.core.components.AuthHeader
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.features.auth.login.components.LoginFooter
import com.yuika.healthtracker.ui.features.auth.login.components.LoginForm
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel? = null,
    onNavigateToClientPage: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val uiState by viewModel?.state?.collectAsStateWithLifecycle() ?: rememberSaveable { mutableStateOf(LoginUiState()) }

    LaunchedEffect(viewModel) {
        viewModel?.effect?.collect { effect ->
            when (effect) {
                is LoginUiEffect.NavigateToDashboard -> onNavigateToClientPage()
                is LoginUiEffect.NavigateToRegister -> onNavigateToRegister()
                is LoginUiEffect.NavigateToForgotPassword -> onNavigateToForgotPassword()
                is LoginUiEffect.showToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = spacing.large,
                    vertical = spacing.extraLarge
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(
                title = "Welcome Back",
                subtitle = "Sign in to continue your progress.",
                icon = Icons.Outlined.FavoriteBorder,
                iconShape = RoundedCornerShape(24.dp),
                iconContainerSize = 104.dp,
                iconSize = 48.dp
            )

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            uiState.errorMessage?.let { msg ->
                ErrorText(msg = msg)
            }

            LoginForm(
                email = uiState.email,
                onEmailChange = { viewModel?.onIntent(LoginUiIntent.EmailChanged(it)) },
                emailError = uiState.emailErrorMessage,
                password = uiState.password,
                onPasswordChange = { viewModel?.onIntent(LoginUiIntent.PasswordChanged(it)) },
                passwordError = uiState.passwordErrorMessage,
                rememberMe = uiState.isRememberAccount,
                onRememberMeChange = { viewModel?.onIntent(LoginUiIntent.RememberAccountClick) },
                passwordVisible = uiState.isShowPassword,
                onPasswordVisibleChange = { viewModel?.onIntent(LoginUiIntent.ShowPasswordClick) },
                isLoading = uiState.isLoading,
                onLoginClick = { viewModel?.onIntent(LoginUiIntent.LoginClick) },
                onForgotPasswordClick = { viewModel?.onIntent(LoginUiIntent.ForgotPasswordClick) }
            )

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            LoginFooter(
                onGoogleClick = { viewModel?.onIntent(LoginUiIntent.GoogleClick) },
                onFacebookClick = { viewModel?.onIntent(LoginUiIntent.FacebookClick) },
                onRegisterClick = { viewModel?.onIntent(LoginUiIntent.RegisterClick) }
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}