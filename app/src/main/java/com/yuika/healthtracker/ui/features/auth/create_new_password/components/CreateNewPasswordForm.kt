package com.yuika.healthtracker.ui.features.auth.create_new_password.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.theme.LocalSpacing
import com.yuika.healthtracker.ui.features.auth.create_new_password.CreateNewPasswordUiState
import com.yuika.healthtracker.ui.features.auth.create_new_password.CreateNewPasswordIntent

@Composable
fun CreateNewPasswordForm(
    modifier: Modifier = Modifier,
    state: CreateNewPasswordUiState,
    onIntent: (CreateNewPasswordIntent) -> Unit
)
{
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "New Password",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(spacing.small))

        OutlinedTextField(
            value = state.newPassword,
            onValueChange = { onIntent(CreateNewPasswordIntent.NewPasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !state.isLoading,
            shape = MaterialTheme.shapes.medium,
            placeholder = { Text("Enter new password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null
                )
            },
            supportingText = {
                if (state.newPasswordError != null)
                {
                    ErrorText(state.newPasswordError)
                }
            },
            trailingIcon = {
                IconButton(onClick = { onIntent(CreateNewPasswordIntent.ShowNewPassword) }, enabled = !state.isLoading) {
                    Icon(
                        imageVector = if (state.isShowNewPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (state.isShowNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordStrengthIndicator(
            strength = state.newPasswordStrength
        )

        Spacer(modifier = Modifier.height(spacing.large))

        // Confirm Password Field
        Text(
            text = "Confirm Password",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(spacing.small))

        OutlinedTextField(
            value = state.confirmNewPassword,
            onValueChange = { onIntent(CreateNewPasswordIntent.ConfirmNewPasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !state.isLoading,
            shape = MaterialTheme.shapes.medium,
            placeholder = { Text("Confirm new password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null
                )
            },
            supportingText = {
                if (state.confirmNewPasswordError != null)
                {
                    ErrorText(state.confirmNewPasswordError)
                }
            },
            trailingIcon = {
                IconButton(onClick = { onIntent(CreateNewPasswordIntent.ShowConfirmNewPassword) }, enabled = !state.isLoading) {
                    Icon(
                        imageVector = if (state.isShowConfirmNewPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (state.isShowConfirmNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onIntent(CreateNewPasswordIntent.ResetPasswordClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                LoadingIndicator()
            } else {
                Text(
                    text = "Reset Password",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
