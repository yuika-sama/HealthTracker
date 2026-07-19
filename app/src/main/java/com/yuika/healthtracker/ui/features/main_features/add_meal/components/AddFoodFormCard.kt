package com.yuika.healthtracker.ui.features.main_features.add_meal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuika.healthtracker.domain.model.FoodCatalog
import com.yuika.healthtracker.ui.core.components.FormTextField
import com.yuika.healthtracker.ui.features.main_features.add_meal.AddMealUiState

@Composable
fun AddFoodFormCard(
    modifier: Modifier = Modifier,
    state: AddMealUiState,
    onFoodNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit,
    onMealTypeChange: (String) -> Unit,
    onManualChange: () -> Unit,
    onFoodCatalogClick: (FoodCatalog) -> Unit
) {
    var unitExpanded by rememberSaveable() { mutableStateOf(false) }
    val units = listOf("Plate (Med)", "Bowl (Smol)", "Serving", "Gram")

    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "FOOD DETAILS",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FormTextField(
            value = state.foodName,
            onValueChange = onFoodNameChange,
            label = "Food name",
            placeholder = "Enter food name",
            errorMessage = state.foodNameError,
            compact = true,
            trailingIcon = {
                if (state.foodName.isNotEmpty()) {
                    IconButton(
                        onClick = { onFoodNameChange("") },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Portion",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormTextField(
                    value = state.quantity,
                    onValueChange = onQuantityChange,
                    modifier = Modifier.weight(0.35f),
                    placeholder = "1",
                    errorMessage = state.quantityError,
                    keyboardType = KeyboardType.Decimal,
                    compact = true
                )

                Column(modifier = Modifier.weight(0.65f)) {
                    Box {
                        FormTextField(
                            value = state.unit,
                            onValueChange = {},
                            readOnly = true,
                            compact = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                        
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { unitExpanded = true }
                        )
                    }
                    
                    DropdownMenu(
                        expanded = unitExpanded,
                        onDismissRequest = { unitExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        units.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onUnitChange(option)
                                    unitExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f))
                .padding(end = 12.dp)
        ) {
            Checkbox(
                checked = state.isManual,
                onCheckedChange = {onManualChange()},
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary,
                    checkmarkColor = MaterialTheme.colorScheme.onSecondary
                )
            )
            Text(
                text = "Manual",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!state.isManual && state.searchResults.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                state.searchResults.forEach { food ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFoodCatalogClick(food) }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = food.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${food.caloriesPerServing} kcal / ${food.defaultQuantity} ${food.unit}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f))
                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Calories",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    FormTextField(
                        value = state.calories,
                        onValueChange = onCaloriesChange,
                        placeholder = "0",
                        errorMessage = state.caloriesError,
                        keyboardType = KeyboardType.Number,
                        compact = true,
                        suffix = {
                            Text(
                                text = "kcal",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Meal type",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    ) {
                        mealTypes.forEach { option ->
                            val isSelected = state.mealType.equals(option, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .padding(3.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .then(
                                        if (isSelected) {
                                            Modifier.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f))
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clickable { onMealTypeChange(option) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.secondary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
