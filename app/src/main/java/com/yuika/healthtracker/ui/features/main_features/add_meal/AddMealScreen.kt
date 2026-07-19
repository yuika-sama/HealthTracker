package com.yuika.healthtracker.ui.features.main_features.add_meal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SuccessText
import com.yuika.healthtracker.ui.features.main_features.add_meal.components.AddFoodFormCard
import com.yuika.healthtracker.ui.features.main_features.add_meal.components.AddedFoodItemCard
import com.yuika.healthtracker.ui.features.main_features.add_meal.components.DashedAddButton
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun AddMealScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    mealType: String,
    dateText: String,
    viewModel: AddMealViewModel = hiltViewModel(),
    onSaveClick: () -> Unit = {}
)
{
    val spacing = LocalSpacing.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(mealType, dateText) {
        viewModel.onIntent(AddMealIntent.Init(mealType, dateText = dateText))
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.effect.collect { effect ->
                when (effect) {
                    is AddMealEffect.NavigateBackWithSuccess -> {
                        Toast.makeText(context, "Save success", Toast.LENGTH_SHORT).show()
                        onSaveClick()
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = spacing.large),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                state.errorMessage?.let { ErrorText(msg = it) }

                if (state.isSuccess && !state.isLoading && state.errorMessage == null) {
                    SuccessText(msg = "Meal saved")
                }

                AddFoodFormCard(
                    state = state,
                    onFoodNameChange = {
                        viewModel.onIntent(AddMealIntent.OnFoodNameChange(it))
                    },
                    onQuantityChange = { viewModel.onIntent(AddMealIntent.OnQuantityChange(it)) },
                    onUnitChange = { viewModel.onIntent(AddMealIntent.OnUnitChange(it)) },
                    onCaloriesChange = { viewModel.onIntent(AddMealIntent.OnCaloriesChange(it)) },
                    onMealTypeChange = { viewModel.onIntent(AddMealIntent.OnMealTypeChange(it)) },
                    onManualChange = {viewModel.onIntent(AddMealIntent.OnManualChange)},
                    onFoodCatalogClick = {viewModel.onIntent(AddMealIntent.OnFoodCatalogClick(it))}
                )
            }

            items(state.addedFoods, key = { it.id }) { food ->
                AddedFoodItemCard(
                    foodName = food.foodName,
                    quantityInfo = "${food.quantity} ${food.unit}",
                    calories = "${food.calories} kcal",
                    onRemoveClick = { viewModel.onIntent(AddMealIntent.OnRemoveFoodClick(food.id)) }
                )
            }

            item {
                DashedAddButton(onClick = { viewModel.onIntent(AddMealIntent.OnAddFoodClick ) })
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            HorizontalDivider(
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )

            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "TOTAL MEAL",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${state.totalCalories} kcal",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "MEAL",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = state.mealType.ifEmpty { "Lunch" },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.onIntent(AddMealIntent.OnSaveMealClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        LoadingIndicator()
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircleOutline,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Save your meal",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
