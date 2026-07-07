package com.yuika.healthtracker.ui.features.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun SocialSection(
    modifier: Modifier = Modifier,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
)
{
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = spacing.small),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
        }
        Spacer(modifier = Modifier.height(spacing.large))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            SocialButton(
                modifier = Modifier.weight(1f),
                text = "Google",
                onClick = onGoogleClick,
                icon = Icons.Default.MarkEmailRead //Google
            )
            SocialButton(
                modifier = Modifier.weight(1f),
                text = "Facebook",
                onClick = onFacebookClick,
                icon = Icons.Default.Facebook
            )
        }
    }
}