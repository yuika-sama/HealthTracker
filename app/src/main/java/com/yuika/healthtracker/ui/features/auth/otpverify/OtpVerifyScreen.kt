package com.yuika.healthtracker.ui.features.auth.otpverify

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.glance.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuika.healthtracker.ui.core.components.AuthHeader
import com.yuika.healthtracker.ui.features.auth.login.LoginUiIntent
import com.yuika.healthtracker.ui.features.auth.otpverify.components.OtpVerifyFooter
import com.yuika.healthtracker.ui.features.auth.otpverify.components.OtpVerifyForm
import com.yuika.healthtracker.ui.features.auth.register.RegisterUiState
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun OtpVerifyScreen(
    modifier: Modifier = Modifier,
    viewModel: OtpVerifyViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {},
    onBackToLoginClick: () -> Unit = {}
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val uiState by viewModel.state?.collectAsStateWithLifecycle() ?: rememberSaveable { mutableStateOf(
        OtpVerifyUiState()
    ) }
    val context = LocalContext.current

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is OtpVerifyEffect.NavigateToHome -> onNavigateToHome()
                is OtpVerifyEffect.NavigateToLogin -> onBackToLoginClick()
                is OtpVerifyEffect.ShowToast -> {
                    // Show toast
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
                shape = MaterialTheme.shapes.large,
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
                        title = "Verification Code",
                        icon = Icons.Outlined.MarkEmailRead,
                        subtitleContent = {
                            Text(
                                text = buildAnnotatedString {
                                    append("Please enter the ${uiState.otpLength}-digit code sent to\nyour email address\n")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)) {
                                        append(uiState.email.ifEmpty { "your email" })
                                    }
                                    append(".")
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    OtpVerifyForm(
                        otpCode = uiState.otpCode,
                        otpLength = uiState.otpLength,
                        onOtpChange = { viewModel?.onIntent(OtpVerifyIntent.OtpCodeChanged(it)) },
                        isLoading = uiState.isLoading,
                        onResendOtp = { viewModel?.onIntent(OtpVerifyIntent.ResendOtp) },
                        onVerify = { viewModel?.onIntent(OtpVerifyIntent.Submit) },
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    OtpVerifyFooter(
                        onBackToLoginClick = onBackToLoginClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtpVerifyScreenPreview() {
    OtpVerifyScreen()
}
