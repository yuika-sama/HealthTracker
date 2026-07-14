package com.yuika.healthtracker.ui.features.main_features.activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuika.healthtracker.ui.theme.Emerald
import com.yuika.healthtracker.ui.theme.InfoBlue

import com.yuika.healthtracker.ui.core.model.IntensityLevel

@Composable
fun ActivityItem(
    modifier: Modifier = Modifier,
    activity: ActivityItemData
) {
    val (icon, iconTint, iconBg) = when (activity.iconType) {
        IntensityLevel.LIGHT -> Triple(
            Icons.AutoMirrored.Outlined.DirectionsWalk,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.surfaceVariant
        )
        IntensityLevel.MEDIUM -> Triple(
            Icons.AutoMirrored.Outlined.DirectionsRun,
            Emerald,
            Emerald.copy(alpha = 0.15f)
        )
        IntensityLevel.STRONG -> Triple(
            Icons.Outlined.LocalFireDepartment,
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
        )
    }

    val (intensityColor, intensityBg, intensityText) = when (activity.intensity) {
        IntensityLevel.LIGHT -> Triple(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.surfaceVariant,
            "Light"
        )
        IntensityLevel.MEDIUM -> Triple(
            Emerald,
            Emerald.copy(alpha = 0.15f),
            "Medium"
        )
        IntensityLevel.STRONG -> Triple(
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
            "Strong"
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Intensity Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(intensityBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = intensityText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = intensityColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Duration
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${activity.durationMins} mins",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }
        }

        // Kcal
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = activity.kcal.toString(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "kcal",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }
    }
}