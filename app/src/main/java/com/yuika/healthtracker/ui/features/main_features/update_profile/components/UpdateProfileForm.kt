package com.yuika.healthtracker.ui.features.main_features.update_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.core.components.DateOfBirthInput
import com.yuika.healthtracker.ui.core.components.FieldErrorText
import com.yuika.healthtracker.ui.core.components.FormTextField
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
        FormTextField(
            value = state.name,
            onValueChange = { onIntent(UpdateProfileIntent.UpdateName(it)) },
            label = "Full name",
            errorMessage = state.nameError,
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Date of birth",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DateOfBirthInput(
                value = state.dateOfBirth,
                isError = state.dateOfBirthError != null,
                enabled = !state.isSaving,
                onDateSelected = { onIntent(UpdateProfileIntent.UpdateDateOfBirth(it)) }
            )
            FieldErrorText(state.dateOfBirthError)
        }

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
            FieldErrorText(state.genderError)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormTextField(
                value = state.weight,
                onValueChange = { onIntent(UpdateProfileIntent.UpdateWeight(it)) },
                label = "Weight",
                errorMessage = state.weightError,
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f),
                enabled = !state.isSaving,
                suffix = { Text("kg") }
            )
            
            FormTextField(
                value = state.height,
                onValueChange = { onIntent(UpdateProfileIntent.UpdateHeight(it)) },
                label = "Height",
                errorMessage = state.heightError,
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f),
                enabled = !state.isSaving,
                suffix = { Text("cm") }
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
            FieldErrorText(state.activityLevelError)
        }

        OutlinedDropdownField(
            label = "Goal",
            selectedOption = state.goal,
            options = goals,
            onOptionSelected = { onIntent(UpdateProfileIntent.UpdateGoal(it)) },
            errorMessage = state.goalError,
            enabled = !state.isSaving
        )
    }
}
