package com.yuika.healthtracker.ui.features.register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
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
import com.yuika.healthtracker.ui.core.components.LabeledField
import com.yuika.healthtracker.ui.core.components.LabeledPasswordField
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    agreedToTerms: Boolean,
    onAgreedToTermsChange: (Boolean) -> Unit,
    onCreateAccountClick: () -> Unit
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
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(spacing.large))

            LabeledField(
                label =  "Email Address",
                value = email,
                onValueChange = onEmailChange,
                placeholder = "name@example.com",
                leadingIcon =  {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null
                    )
                }
            )
            Spacer(modifier = Modifier.height(spacing.large))

            LabeledPasswordField(
                label = "Password",
                value = password,
                onValueChange = onPasswordChange,
                placeholder = "••••••••",
                visible = passwordVisible,
                onToggleVisible = { onPasswordVisibleChange(!passwordVisible) },
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
                onToggleVisible = { onConfirmPasswordVisibleChange(!confirmPasswordVisible) },
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
                    onCheckedChange = onAgreedToTermsChange
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
                onClick = onCreateAccountClick,
                enabled = agreedToTerms,
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
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.width(spacing.small))
                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}