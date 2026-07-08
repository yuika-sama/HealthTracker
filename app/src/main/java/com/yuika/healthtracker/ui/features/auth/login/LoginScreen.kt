package com.yuika.healthtracker.ui.features.auth.login

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.core.components.AuthHeader
import com.yuika.healthtracker.ui.features.auth.login.components.LoginFooter
import com.yuika.healthtracker.ui.features.auth.login.components.LoginForm
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
){
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

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
        ){
            AuthHeader(
                title = "Welcome Back",
                subtitle = "Sign in to continue your progress.",
                icon = Icons.Outlined.FavoriteBorder,
                iconShape = RoundedCornerShape(24.dp),
                iconContainerSize = 104.dp,
                iconSize = 48.dp
            )

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            LoginForm(
                onLoginClick = onLoginClick,
                onForgotPasswordClick = onForgotPasswordClick
            )

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            LoginFooter(
                onGoogleClick = onGoogleClick,
                onFacebookClick = onFacebookClick,
                onRegisterClick = onRegisterClick
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen()
}