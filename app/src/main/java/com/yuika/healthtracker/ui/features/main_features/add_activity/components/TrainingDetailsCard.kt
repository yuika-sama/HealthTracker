package com.yuika.healthtracker.ui.features.main_features.add_activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuika.healthtracker.ui.features.main_features.add_activity.AddActivityIntent
import com.yuika.healthtracker.ui.features.main_features.add_activity.AddActivityUiState
import com.yuika.healthtracker.ui.theme.Emerald
import com.yuika.healthtracker.ui.theme.EnergyAmber

import com.yuika.healthtracker.ui.core.model.IntensityLevel

@Composable
fun TrainingDetailsCard(
    modifier: Modifier = Modifier,
    state: AddActivityUiState,
    onIntent: (AddActivityIntent) -> Unit
) {
    val intensities = IntensityLevel.entries

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Training time",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = state.duration,
                onValueChange = { onIntent(AddActivityIntent.OnDurationChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.durationError != null,
                supportingText = state.durationError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text("minutes") },
                colors = textFieldColors()
            )
        }

        // Cường độ
        Column {
            Text(
                text = "Intensity",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                intensities.forEach { intensity ->
                    val isSelected = state.selectedIntensity == intensity
                    val bgColor = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.background
                    val borderColor = if (isSelected) Emerald else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    val textColor = if (isSelected) Emerald else MaterialTheme.colorScheme.onBackground
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                            .clickable { onIntent(AddActivityIntent.OnIntensityChange(intensity)) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = intensity.displayName,
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                            color = textColor
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f))
                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF3E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocalFireDepartment,
                    contentDescription = null,
                    tint = EnergyAmber
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "Estimated calories burned",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = state.estimatedKcalBurned.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    }
}
