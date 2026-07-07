package com.yuika.healthtracker.ui.features.main_features.diary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    valueColor: Color,
    bgColor: Color,
    labelColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
) {
    val isOutline = bgColor == MaterialTheme.colorScheme.background

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .let {
                if (isOutline) {
                    it.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                } else {
                    it
                }
            }
            .background(bgColor)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = labelColor
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = valueColor,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "kcal",
            style = MaterialTheme.typography.labelSmall,
            color = labelColor
        )
    }
}