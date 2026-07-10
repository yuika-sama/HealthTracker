package com.yuika.healthtracker.ui.features.auth.forgot_password

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.AuthHeader
import com.yuika.healthtracker.ui.features.auth.forgot_password.components.ForgotPasswordFooter
import com.yuika.healthtracker.ui.features.auth.forgot_password.components.ForgotPasswordForm
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onBackToLoginClick: () -> Unit = {},
    onSendCodeClick: (String) -> Unit = {}
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect)
                {
                    is ForgotPasswordUiEffect.NavigateToLogin -> onBackToLoginClick()
                    is ForgotPasswordUiEffect.NavigateToVerifyOtp -> onSendCodeClick(effect.email)
                    is ForgotPasswordUiEffect.ShowToast ->
                    {
                        Toast
                            .makeText(context, effect.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.large),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.large, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AuthHeader(
                        title = "Forgot Password?",
                        subtitle = "Enter your email address and we'll send you an OTP code to reset your password.",
                        icon = Icons.Outlined.LockReset
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    ForgotPasswordForm(
                        state = state.value,
                        onIntent = viewModel::onIntent
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    ForgotPasswordFooter(
                        onBackToLoginClick = { viewModel.onIntent(ForgotPasswordUiIntent.LoginClick) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview()
{
    ForgotPasswordScreen()
}
