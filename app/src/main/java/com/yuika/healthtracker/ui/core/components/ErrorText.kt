package com.yuika.healthtracker.ui.core.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun ErrorText(msg: String = ""){
    val spacing = LocalSpacing.current
    Text(
        text = msg,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = spacing.medium)
    )
    Spacer(modifier = Modifier.height(spacing.small))
}