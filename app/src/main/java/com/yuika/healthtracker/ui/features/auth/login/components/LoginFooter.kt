package com.yuika.healthtracker.ui.features.auth.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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