package com.yuika.healthtracker.ui.features.auth.forgot_password.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.theme.LocalSpacing
import com.yuika.healthtracker.ui.features.auth.forgot_password.ForgotPasswordUiState
import com.yuika.healthtracker.ui.features.auth.forgot_password.ForgotPasswordUiIntent
import com.yuika.healthtracker.ui.core.components.LoadingIndicator

@Composable
fun ForgotPasswordForm(
    modifier: Modifier = Modifier,
    state: ForgotPasswordUiState,
    onIntent: (ForgotPasswordUiIntent) -> Unit
)
{
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Email Address",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(spacing.small))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onIntent(ForgotPasswordUiIntent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.emailError != null,
            enabled = !state.isLoading,
            supportingText = {
                if (state.emailError != null)
                {
                    ErrorText(state.emailError)
                }
            },
            shape = MaterialTheme.shapes.medium,
            placeholder = { Text("name@example.com") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(spacing.large))

        Button(
            onClick = { onIntent(ForgotPasswordUiIntent.SubmitClick) },
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (state.isLoading) {
                LoadingIndicator()
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Send Code",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(spacing.small))
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
