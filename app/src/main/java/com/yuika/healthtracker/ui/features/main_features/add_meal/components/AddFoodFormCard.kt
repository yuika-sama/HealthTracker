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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuika.healthtracker.domain.model.FoodCatalog
import com.yuika.healthtracker.ui.features.main_features.add_meal.AddMealIntent
import com.yuika.healthtracker.ui.features.main_features.add_meal.AddMealUiState
import com.yuika.healthtracker.ui.theme.Emerald
import com.yuika.healthtracker.ui.theme.ErrorRed

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

    var isManual by rememberSaveable() { mutableStateOf(false) }
    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Food name",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = state.foodName,
                onValueChange = onFoodNameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.foodNameError != null,
                supportingText = state.foodNameError?.let { { Text(it) } },
                trailingIcon = {
                    if (state.foodName.isNotEmpty()) {
                        IconButton(onClick = { onFoodNameChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = ErrorRed
                            )
                        }
                    }
                },
                colors = textFieldColors()
            )
        }

        Column {
            Text(
                text = "Portion",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.quantity,
                    onValueChange = onQuantityChange,
                    modifier = Modifier.weight(0.35f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = state.quantityError != null,
                    supportingText = state.quantityError?.let { { Text(it) } },
                    colors = textFieldColors()
                )

                Box(modifier = Modifier.weight(0.65f)) {
                    OutlinedTextField(
                        value = state.unit,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors()
                    )
                    
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Transparent)
                            .clickable { unitExpanded = true }
                    )
                    
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.isManual,
                onCheckedChange = {onManualChange()},
                colors = CheckboxDefaults.colors(checkedColor = Emerald)
            )
            Text(
                text = "Manual",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }

        if (!state.isManual && state.searchResults.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            ) {
                state.searchResults.forEach { food ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFoodCatalogClick(food) }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(food.name, fontWeight = FontWeight.Medium)
                        Text("${food.caloriesPerServing} kcal / ${food.defaultQuantity} ${food.unit}")
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f))
                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Calories input row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nutritional content",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    
                    OutlinedTextField(
                        value = state.calories,
                        onValueChange = onCaloriesChange,
                        modifier = Modifier.width(100.dp),
                        singleLine = true,
                        isError = state.caloriesError != null,
                        supportingText = state.caloriesError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Emerald,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )
                    
                    Text(
                        text = "Calories (Kcal)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Emerald.copy(alpha = 0.05f))
                ) {
                    mealTypes.forEach { option ->
                        val isSelected = state.mealType.equals(option, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .clickable { onMealTypeChange(option) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                color = if (isSelected) Emerald else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
    focusedLabelColor = MaterialTheme.colorScheme.secondary,
    cursorColor = MaterialTheme.colorScheme.secondary,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)
