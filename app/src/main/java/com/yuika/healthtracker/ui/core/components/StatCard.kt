package com.yuika.healthtracker.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String? = "kcal",
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    iconBgColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    valueColor: Color = MaterialTheme.colorScheme.onBackground,
    labelColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
    bgColor: Color = MaterialTheme.colorScheme.background,
    showContainer: Boolean = true,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    if (showContainer) {
        val isOutline = bgColor == MaterialTheme.colorScheme.background
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .let {
                    if (isOutline) {
                        it.border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            RoundedCornerShape(16.dp)
                        )
                    } else {
                        it
                    }
                }
                .background(bgColor)
                .padding(16.dp),
            horizontalAlignment = horizontalAlignment
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = labelColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (unit != null && horizontalAlignment != Alignment.CenterHorizontally) {
                // If aligned left/right and has unit, show value & unit stacked or adjacent
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = valueColor
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelMedium,
                    color = labelColor
                )
            } else if (unit != null) {
                // If centered
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = valueColor,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall,
                    color = labelColor
                )
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = valueColor
                )
            }
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = labelColor,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = valueColor
            )
        }
    }
}
