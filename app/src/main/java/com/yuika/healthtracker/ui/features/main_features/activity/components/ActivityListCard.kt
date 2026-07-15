package com.yuika.healthtracker.ui.features.main_features.activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.core.model.IntensityLevel

data class ActivityItemData(
    val title: String,
    val intensity: IntensityLevel,
    val durationMins: Int,
    val kcal: Int,
    val iconType: IntensityLevel
)

@Composable
fun ActivityListCard(
    modifier: Modifier = Modifier,
    activities: List<ActivityItemData>
)
{
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                RoundedCornerShape(16.dp)
            )
            .background(MaterialTheme.colorScheme.background)
    ) {
        activities.forEachIndexed { index, activity ->
            ActivityItem(activity = activity)
            if (index < activities.lastIndex){
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = DividerDefaults.Thickness,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
            }
        }
    }
}
