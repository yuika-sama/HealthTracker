package com.yuika.healthtracker.ui.features.main_features.activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuika.healthtracker.ui.theme.Emerald

@Composable
fun ActivitySummaryCard(
    modifier: Modifier = Modifier,
    burnedKcal: Int = 450,
    goalKcal: Int = 600
) {
    val progress = burnedKcal.toFloat() / goalKcal.toFloat()
    val percentage = (progress * 100).toInt()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular Progress
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(80.dp)
        ) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(80.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                strokeWidth = 8.dp,
                strokeCap = StrokeCap.Round
            )
            
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(80.dp),
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 8.dp,
                strokeCap = StrokeCap.Round
            )
            
            Icon(
                imageVector = Icons.Outlined.LocalFireDepartment,
                contentDescription = "Burned",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(24.dp))
        
        // Text Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Total Burned",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = burnedKcal.toString(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "kcal",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Linear Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(6.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = "$percentage% of $goalKcal kcal daily goal",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }
    }
}
