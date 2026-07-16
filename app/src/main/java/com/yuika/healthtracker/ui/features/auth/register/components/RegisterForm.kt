package com.yuika.healthtracker.ui.features.auth.register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.core.components.LabeledField
import com.yuika.healthtracker.ui.core.components.LabeledPasswordField
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.theme.LocalSpacing
import com.yuika.healthtracker.ui.features.auth.register.RegisterUiState
import com.yuika.healthtracker.ui.features.auth.register.RegisterIntent

@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit
)
{
    val spacing = LocalSpacing.current

    Card(
        modifier= modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(spacing.large)
        ) {
            LabeledField(
                label = "Full Name",
                value = state.fullName,
                onValueChange = { onIntent(RegisterIntent.FullNameChanged(it)) },
                placeholder = "John Doe",
                supportingText = state.fullNameError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                },
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(spacing.large))

            LabeledField(
                label =  "Email Address",
                value = state.email,
                onValueChange = { onIntent(RegisterIntent.EmailChanged(it)) },
                placeholder = "name@example.com",
                supportingText = state.emailError,
                enabled = !state.isLoading,
                leadingIcon =  {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(spacing.medium))

            LabeledField(
                value = state.dateOfBirth,
                onValueChange = { onIntent(RegisterIntent.DateOfBirthChanged(it)) },
                label = "Date of birth",
                placeholder = "yyyy-MM-dd",
                supportingText = state.dateOfBirthError,
                enabled = !state.isLoading,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(spacing.large))

            LabeledPasswordField(
                label = "Password",
                value = state.password,
                onValueChange = { onIntent(RegisterIntent.PasswordChanged(it)) },
                placeholder = "••••••••",
                visible = state.showPassword,
                onToggleVisible = { onIntent(RegisterIntent.ShowPasswordChanged) },
                supportingText = state.passwordError,
                enabled = !state.isLoading,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(spacing.large))

            LabeledPasswordField(
                label = "Confirm password",
                value = state.confirmPassword,
                onValueChange = { onIntent(RegisterIntent.ConfirmPasswordChanged(it)) },
                placeholder = "••••••••",
                visible = state.showConfirmPassword,
                onToggleVisible = { onIntent(RegisterIntent.ShowConfirmPasswordChanged) },
                supportingText = state.confirmPasswordError,
                enabled = !state.isLoading,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CheckBoxOutlineBlank,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(spacing.large))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.agreedToTerms,
                    onCheckedChange = { onIntent(RegisterIntent.AgreedToTermsChanged) },
                    enabled = !state.isLoading
                )
                Spacer(modifier = Modifier.width(spacing.small))
                Text(
                    text = "I agree to the Terms of Service and Privacy Policy",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier= Modifier.height(spacing.large))

            Button(
                onClick = { onIntent(RegisterIntent.Submit) },
                enabled = state.agreedToTerms && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            ) {
                if (!state.isLoading){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Create Account",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.width(spacing.small))
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = null
                        )
                    }
                } else {
                    LoadingIndicator()
                }
            }
        }
    }
}
