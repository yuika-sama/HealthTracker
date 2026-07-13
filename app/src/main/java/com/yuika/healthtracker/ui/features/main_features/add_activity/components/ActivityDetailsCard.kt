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
import androidx.compose.material.icons.automirrored.outlined.DirectionsBike
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.features.main_features.add_activity.AddActivityIntent
import com.yuika.healthtracker.ui.features.main_features.add_activity.AddActivityUiState
import com.yuika.healthtracker.ui.theme.Emerald

@Composable
fun ActivityDetailsCard(
    modifier: Modifier = Modifier,
    state: AddActivityUiState,
    onIntent: (AddActivityIntent) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .background( MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACTIVITY INFO",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        Column {
            Text(
                text = "Activity name",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = state.activityName,
                onValueChange = { onIntent(AddActivityIntent.OnActivityNameChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors()
            )
        }

        Column {
            Text(
                text = "Activity icon",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActivityIconItem(
                    icon = Icons.AutoMirrored.Outlined.DirectionsRun,
                    isSelected = state.selectedIcon == "run",
                    onClick = { onIntent(AddActivityIntent.OnIconChange("run")) }
                )
                ActivityIconItem(
                    icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                    isSelected = state.selectedIcon == "walk",
                    onClick = { onIntent(AddActivityIntent.OnIconChange("walk")) }
                )
                ActivityIconItem(
                    icon = Icons.AutoMirrored.Outlined.DirectionsBike,
                    isSelected = state.selectedIcon == "bike",
                    onClick = { onIntent(AddActivityIntent.OnIconChange("bike")) }
                )
                ActivityIconItem(
                    icon = Icons.Outlined.FitnessCenter,
                    isSelected = state.selectedIcon == "gym",
                    onClick = { onIntent(AddActivityIntent.OnIconChange("gym")) }
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                        .clickable { /* TODO */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Icon",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Column {
            Text(
                text = "Kcal / hour",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = state.kcalPerHour,
                onValueChange = { onIntent(AddActivityIntent.OnKcalPerHourChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text("kcal/h") },
                colors = textFieldColors()
            )
        }
    }
}


@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
    focusedLabelColor = MaterialTheme.colorScheme.secondary,
    cursorColor = MaterialTheme.colorScheme.secondary,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White
)
