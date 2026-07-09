package com.yuika.healthtracker.ui.features.auth.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuika.healthtracker.ui.core.components.ClickableTextLink
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun LoginFooter(
    modifier: Modifier = Modifier,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onRegisterClick: () -> Unit
)
{
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        SocialSection(
            onGoogleClick = onGoogleClick,
            onFacebookClick = onFacebookClick
        )

        Spacer(modifier = Modifier.height(spacing.extraLarge))

        ClickableTextLink(
            descriptionText = "Don't have an account? ",
            linkText = "Register",
            onClick = onRegisterClick
        )
    }
}