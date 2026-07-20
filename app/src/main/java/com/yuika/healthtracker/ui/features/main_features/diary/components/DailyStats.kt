package com.yuika.healthtracker.ui.features.main_features.diary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.R
import com.yuika.healthtracker.ui.core.components.StatCard
import com.yuika.healthtracker.ui.theme.Emerald
import com.yuika.healthtracker.ui.theme.EnergyAmber

@Composable
fun DailyStats(
    modifier: Modifier = Modifier,
    eatenKcal: Int = 1250,
    remainingKcal: Int = 750,
    burnedKcal: Int = 320
)
{
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.stat_eaten),
            value = eatenKcal.toString(),
            valueColor = MaterialTheme.colorScheme.secondary,
            bgColor = MaterialTheme.colorScheme.background,
            horizontalAlignment = Alignment.CenterHorizontally
        )

        StatCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.stat_remaining),
            value = remainingKcal.toString(),
            valueColor = MaterialTheme.colorScheme.onBackground,
            bgColor = MaterialTheme.colorScheme.secondary,
            labelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        )

        StatCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.stat_burned),
            value = burnedKcal.toString(),
            valueColor = EnergyAmber,
            bgColor = MaterialTheme.colorScheme.background,
            horizontalAlignment = Alignment.CenterHorizontally
        )
    }
}
