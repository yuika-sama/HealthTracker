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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.core.components.AuthHeader
import com.yuika.healthtracker.ui.core.components.ClickableTextLink
import com.yuika.healthtracker.ui.features.auth.register.components.RegisterForm
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onNavigateToVerifyOtp: (String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

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

            RegisterForm(
                fullName = fullName,
                onFullNameChange = { fullName = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = { passwordVisible = it },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordVisibleChange = { confirmPasswordVisible = it },
                agreedToTerms = agreedToTerms,
                onAgreedToTermsChange = { agreedToTerms = it },
                onCreateAccountClick = { onNavigateToVerifyOtp(email) }
            )

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            ClickableTextLink(
                descriptionText = "Already have an account? ",
                linkText = "Log in",
                onClick = onNavigateToLogin
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
