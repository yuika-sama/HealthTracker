package com.yuika.healthtracker.ui.features.main_features.update_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.core.components.SegmentedSelector
import com.yuika.healthtracker.ui.core.components.OutlinedDropdownField
import com.yuika.healthtracker.ui.features.main_features.update_profile.UpdateProfileUiState
import com.yuika.healthtracker.ui.features.main_features.update_profile.UpdateProfileIntent

@Composable
fun UpdateProfileForm(
    modifier: Modifier = Modifier,
    state: UpdateProfileUiState,
    onIntent: (UpdateProfileIntent) -> Unit
) {
    val goals = listOf("Lose weight", "Maintain weight", "Weight gain")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { onIntent(UpdateProfileIntent.UpdateName(it)) },
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = state.age,
            onValueChange = { onIntent(UpdateProfileIntent.UpdateAge(it)) },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = textFieldColors()
        )

        Column {
            Text(
                text = "Gender",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SegmentedSelector(
                options = listOf("Male", "Female"),
                selectedOption = state.gender,
                onOptionSelected = { onIntent(UpdateProfileIntent.UpdateGender(it)) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.weight,
                onValueChange = { onIntent(UpdateProfileIntent.UpdateWeight(it)) },
                label = { Text("Weight") },
                suffix = { Text("kg") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = textFieldColors()
            )
            
            OutlinedTextField(
                value = state.height,
                onValueChange = { onIntent(UpdateProfileIntent.UpdateHeight(it)) },
                label = { Text("Height") },
                suffix = { Text("cm") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = textFieldColors()
            )
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Activity level",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = "${state.activityLevel.toInt()}/5",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            Slider(
                value = state.activityLevel,
                onValueChange = { onIntent(UpdateProfileIntent.UpdateActivityLevel(it)) },
                valueRange = 1f..5f,
                steps = 3, // 5 distinct values (1,2,3,4,5)
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    activeTickColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                    inactiveTickColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sedentary", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Text("Active", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
        }

        OutlinedDropdownField(
            label = "Goal",
            selectedOption = state.goal,
            options = goals,
            onOptionSelected = { onIntent(UpdateProfileIntent.UpdateGoal(it)) }
        )
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
    focusedLabelColor = MaterialTheme.colorScheme.secondary,
    cursorColor = MaterialTheme.colorScheme.secondary
)
