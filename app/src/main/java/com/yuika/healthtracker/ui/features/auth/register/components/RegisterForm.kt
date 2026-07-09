package com.yuika.healthtracker.ui.features.auth.register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.ArrowForward
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
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LabeledField
import com.yuika.healthtracker.ui.core.components.LabeledPasswordField
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    fullNameError: String?,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String?,
    passwordVisible: Boolean,
    onPasswordVisibleChange: () -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordError: String?,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibleChange: () -> Unit,
    agreedToTerms: Boolean,
    onAgreedToTermsChange: () -> Unit,
    isLoading: Boolean,
    onRegisterClick: () -> Unit
)
{
    val spacing = LocalSpacing.current

    Card(
        modifier= modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
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
                value = fullName,
                onValueChange = onFullNameChange,
                placeholder = "John Doe",
                supportingText = fullNameError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                },
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(spacing.large))

            LabeledField(
                label =  "Email Address",
                value = email,
                onValueChange = onEmailChange,
                placeholder = "name@example.com",
                supportingText = emailError,
                leadingIcon =  {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null
                    )
                },
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(spacing.large))

            LabeledPasswordField(
                label = "Password",
                value = password,
                onValueChange = onPasswordChange,
                placeholder = "••••••••",
                visible = passwordVisible,
                onToggleVisible = onPasswordVisibleChange,
                supportingText = passwordError,
                enabled = !isLoading,
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
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                placeholder = "••••••••",
                visible = confirmPasswordVisible,
                onToggleVisible = onConfirmPasswordVisibleChange,
                supportingText = confirmPasswordError,
                enabled = !isLoading,
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
                    checked = agreedToTerms,
                    onCheckedChange = {onAgreedToTermsChange()},
                    enabled = !isLoading
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
                onClick = onRegisterClick,
                enabled = agreedToTerms && !isLoading,
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
                if (!isLoading){
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