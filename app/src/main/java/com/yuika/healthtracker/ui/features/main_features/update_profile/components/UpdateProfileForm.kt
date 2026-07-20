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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.R
import com.yuika.healthtracker.ui.core.components.DateOfBirthInput
import com.yuika.healthtracker.ui.core.components.FieldErrorText
import com.yuika.healthtracker.ui.core.components.FormTextField
import com.yuika.healthtracker.ui.core.components.SegmentedSelector
import com.yuika.healthtracker.ui.core.components.OutlinedDropdownField
import com.yuika.healthtracker.ui.core.i18n.activityLevelTitle
import com.yuika.healthtracker.ui.core.i18n.genderLabel
import com.yuika.healthtracker.ui.core.i18n.goalTitle
import com.yuika.healthtracker.ui.features.main_features.update_profile.UpdateProfileUiState
import com.yuika.healthtracker.ui.features.main_features.update_profile.UpdateProfileIntent

@Composable
fun UpdateProfileForm(
    modifier: Modifier = Modifier,
    state: UpdateProfileUiState,
    onIntent: (UpdateProfileIntent) -> Unit
) {
    val goals = listOf("lose_weight", "maintain_weight", "gain_weight")

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
            label = stringResource(R.string.update_profile_full_name),
            errorMessage = state.nameErrorRes?.let { stringResource(it) },
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.update_profile_date_of_birth),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DateOfBirthInput(
                value = state.dateOfBirth,
                isError = state.dateOfBirthErrorRes != null,
                enabled = !state.isSaving,
                onDateSelected = { onIntent(UpdateProfileIntent.UpdateDateOfBirth(it)) }
            )
            FieldErrorText(state.dateOfBirthErrorRes?.let { stringResource(it) })
        }

        Column {
            Text(
                text = stringResource(R.string.update_profile_gender),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SegmentedSelector(
                options = listOf("Male", "Female"),
                selectedOption = state.gender,
                onOptionSelected = { onIntent(UpdateProfileIntent.UpdateGender(it)) },
                labelProvider = { genderLabel(it) }
            )
            FieldErrorText(state.genderErrorRes?.let { stringResource(it) })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormTextField(
                value = state.weight,
                onValueChange = { onIntent(UpdateProfileIntent.UpdateWeight(it)) },
                label = stringResource(R.string.stat_weight),
                errorMessage = state.weightErrorRes?.let { stringResource(it) },
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f),
                enabled = !state.isSaving,
                suffix = { Text(stringResource(R.string.unit_kg)) }
            )
            
            FormTextField(
                value = state.height,
                onValueChange = { onIntent(UpdateProfileIntent.UpdateHeight(it)) },
                label = stringResource(R.string.stat_height),
                errorMessage = state.heightErrorRes?.let { stringResource(it) },
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.weight(1f),
                enabled = !state.isSaving,
                suffix = { Text(stringResource(R.string.unit_cm)) }
            )
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.update_profile_activity_level),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = stringResource(R.string.update_profile_activity_level_value, state.activityLevel.toInt()),
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
                Text(activityLevelTitle("sedentary"), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Text(activityLevelTitle("moderately_active"), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            FieldErrorText(state.activityLevelErrorRes?.let { stringResource(it) })
        }

        OutlinedDropdownField(
            label = stringResource(R.string.update_profile_goal),
            selectedOption = state.goal,
            options = goals,
            onOptionSelected = { onIntent(UpdateProfileIntent.UpdateGoal(it)) },
            errorMessage = state.goalErrorRes?.let { stringResource(it) },
            enabled = !state.isSaving,
            labelProvider = { goalTitle(it) }
        )
    }
}
