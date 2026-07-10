package com.yuika.healthtracker.ui.features.auth.otpverify.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OtpInputFields(
    otpCode: String,
    otpLength: Int,
    onOtpChange: (String) -> Unit
)
{
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0 until otpLength)
        {
            val char = otpCode.getOrNull(i)?.toString() ?: ""
            OutlinedTextField(
                value = char,
                onValueChange = { newValue ->
                    if (newValue.isEmpty())
                    {
                        // Backspace
                        val newOtp = buildString {
                            for (j in 0 until otpLength)
                            {
                                if (j == i) append("")
                                else append(otpCode.getOrNull(j) ?: "")
                            }
                        }
                        onOtpChange(newOtp)

                        if (i > 0){
                            focusRequesters[i-1].requestFocus()
                        }
                    }
                    else
                    {
                        // add a char/replace input
                        val charToUse = newValue.last().toString()
                        val newOtp = buildString {
                            for (j in 0 until otpLength){
                                if (j == i) append(charToUse)
                                else append(otpCode.getOrNull(j) ?: "")
                            }
                        }
                        onOtpChange(newOtp)

                        if (i < otpLength - 1) {
                            focusRequesters[i+1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequesters[i]),
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (i == otpLength - 1) ImeAction.Done else ImeAction.Next
                ),
                singleLine = true
            )
        }
    }

    LaunchedEffect(Unit) {
        if (focusRequesters.isNotEmpty())
        {
            focusRequesters[0].requestFocus()
        }
    }
}