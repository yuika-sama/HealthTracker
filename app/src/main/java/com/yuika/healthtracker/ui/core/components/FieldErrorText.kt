package com.yuika.healthtracker.ui.core.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.core.i18n.localizedMessage

@Composable
fun FieldErrorText(message: String?) {
    if (message != null) {
        Text(
            text = localizedMessage(message),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
