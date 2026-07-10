package com.yuika.healthtracker.ui.features.auth.otpverify.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.features.auth.otpverify.OtpVerifyIntent
import com.yuika.healthtracker.ui.features.auth.otpverify.OtpVerifyUiState
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun OtpVerifyForm(
    modifier: Modifier = Modifier,
    state: OtpVerifyUiState,
    onIntent: (OtpVerifyIntent) -> Unit
)
{
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OtpInputFields(
            otpCode = state.otpCode,
            otpLength = state.otpLength,
            onOtpChange = { onIntent(OtpVerifyIntent.OtpCodeChanged(it)) },
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Didn't receive the code?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(spacing.small))

        Row {
            Text(
                text = "Resend Code",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(enabled = !state.isLoading) {
                    onIntent(OtpVerifyIntent.ResendOtp)
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            // TODO: Countdown timer
            Text(
                text = "(00:57)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onIntent(OtpVerifyIntent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = state.otpCode.length == state.otpLength && !state.isLoading
        ) {
            if (state.isLoading)
            {
                LoadingIndicator()
            }
            else
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Verify",
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
