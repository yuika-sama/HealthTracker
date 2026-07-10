package com.yuika.healthtracker.ui.features.auth.register

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuika.healthtracker.ui.core.components.AuthHeader
import com.yuika.healthtracker.ui.core.components.ClickableTextLink
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.features.auth.register.components.RegisterForm
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToVerifyOtp: (String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect)
            {
                is RegisterEffect.NavigateToVerifyOtp -> onNavigateToVerifyOtp(effect.email)
                is RegisterEffect.NavigateToLogin -> onNavigateToLogin()
                is RegisterEffect.ShowToast ->
                {
                    // show toast
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
                title = "Create Account",
                subtitle = "Sign up to start your progress.",
                icon = Icons.Outlined.FavoriteBorder,
                iconShape = RoundedCornerShape(24.dp),
                iconContainerSize = 104.dp,
                iconSize = 48.dp
            )

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            uiState.errorMessage?.let { msg ->
                ErrorText(msg = msg)
            }

            RegisterForm(
                state = uiState,
                onIntent = viewModel::onIntent
            )

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            ClickableTextLink(
                descriptionText = "Already have an account? ",
                linkText = "Log in",
                onClick = { onNavigateToLogin() }
            )

            Spacer(modifier = Modifier.height(spacing.small))
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview()
{
    RegisterScreen()
}
